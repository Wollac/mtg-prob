package probability;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import probability.checker.Hand;
import probability.checker.PlayableChecker;
import probability.config.Config;
import probability.config.Settings;
import probability.core.Card;
import probability.core.Deck;
import probability.core.MulliganRule;
import probability.core.Spell;
import probability.csv.AbstractCSVParser.CvsParseException;
import probability.csv.LandCSVParser;
import probability.csv.SpellCSVParser;

public class Main {

    public static void main(String[] args) {

        Deck deck = buildDeck(Settings.config);

        if (deck == null) {
            System.err.println("No deck has been loaded.");
            return;
        }

        System.out.println("The following deck has been loaded:");
        System.out.println(deck.toFormattedString());

        MulliganRule mulliganRule = new MulliganRule(new File("mulligan.txt"));

        System.out.println("Taking a mulligan, if one of the following rules applies:");
        System.out.println(mulliganRule.toFormattedString());

        Set<Integer> convertedManaCosts = getConvertedManaCosts(deck);

        int minCmc = Collections.min(convertedManaCosts);
        int maxCmc = Collections.min(convertedManaCosts);

        System.out.println("Calculating the combined failure probability for" + " the given spells:");

        for (int turn = minCmc; turn <= maxCmc + 3; turn++) {

            int playable = countPlayable(deck, mulliganRule, turn, Settings.config);
            double factor = 1.0 - (double) playable / Settings.config.sampleSize();

            System.out.printf("Turn %02d: %4.1f%%%n", turn, factor * 100.0);
        }

    }

    private static Set<Integer> getConvertedManaCosts(Deck deck) {

        Set<Integer> result = new HashSet<>();

        for (Card card : deck.cards()) {

            if (card instanceof Spell) {
                result.add(((Spell) card).getCMC());
            }
        }

        return result;
    }

    private static Deck buildDeck(Config config) {

        Deck deck = new Deck(config);

        addLands(deck);
        addSpells(deck);

        if (deck.isEmpty()) {
            return null;
        }

        deck.fillWithDummies();

        return deck;
    }

    private static void addLands(Deck deck) {

        try (Reader reader = new FileReader("lands.csv")) {
            LandCSVParser parser = new LandCSVParser(reader);

            deck.addAll(parser.readAll());
        } catch (IOException e) {
            System.err.println("Could not read csv file: " + e.getMessage());
        } catch (CvsParseException e) {
            System.err.println("Error parsing csv file: " + e.getCause());
        }
    }

    private static void addSpells(Deck deck) {

        try (Reader reader = new FileReader("spells.csv")) {
            SpellCSVParser parse = new SpellCSVParser(reader);

            deck.addAll(parse.readAll());
        } catch (IOException e) {
            System.err.println("Could not read csv file: " + e.getMessage());
        } catch (CvsParseException e) {
            System.err.println("Error parsing csv file: " + e.getCause());
        }
    }

    private static int countPlayable(Deck deck, MulliganRule mulliganRule, int turn, Config config) {

        int good = 0;
        for (int i = 0; i < config.sampleSize(); i++) {

            Hand hand = getStartingHand(deck, mulliganRule, turn, config);

            PlayableChecker checker = new PlayableChecker(deck, hand);

            if (checker.isPlayable(turn)) {
                good++;
            }
        }

        return good;
    }

    private static Hand getStartingHand(Deck deck, MulliganRule mulliganRule, int turn, Config config) {

        Hand hand = null;
        for (int mulligan = 0; mulligan < config.initialHandSize(); mulligan++) {

            deck.shuffle();
            hand = deck.draw(turn, mulligan);

            if (!mulliganRule.takeMulligan(hand.getStartingHand())) {
                break;
            }

            mulligan++;
        }

        return hand;
    }

}
