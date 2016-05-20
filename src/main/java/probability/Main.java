package probability;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.Set;

import probability.checker.PlayableChecker;
import probability.config.Settings;
import probability.core.CardUtils;
import probability.core.Deck;
import probability.core.MulliganRule;
import probability.csv.AbstractCSVParser.CvsParseException;
import probability.csv.LandCSVParser;
import probability.csv.SpellCSVParser;
import probability.messages.Messages;

public class Main {

    public static void main(String[] args) {

        Deck deck = buildDeck();

        if (deck == null) {
            System.err.println("No deck has been loaded.");
            return;
        }

        System.out.println(deck.toFormattedString());

        MulliganRule mulliganRule = new MulliganRule(new File(Settings.MULLIGAN_RULES_FILE_NAME));

        System.out.println(Messages.get().takeMulligan());
        System.out.println(mulliganRule.toFormattedString());

        Set<Integer> convertedManaCosts = CardUtils.getConvertedManaCosts(deck.cards());

        int minCmc = Collections.min(convertedManaCosts);
        int maxCmc = Collections.min(convertedManaCosts);

        System.out.println(Messages.get().combinedFailureProbability());

        PlayableChecker checker = new PlayableChecker(deck, mulliganRule);

        for (int turn = minCmc; turn <= maxCmc + Settings.config.turnsAfterMaxCMC(); turn++) {

            int playable = checker.countPlayable(turn);
            double factor = 1.0 - (double) playable / Settings.config.sampleSize();

            System.out.println(Messages.get().probability(turn, factor));
        }
    }

    private static Deck buildDeck() {

        Deck deck = new Deck();

        addLands(deck);
        addSpells(deck);

        if (deck.isEmpty()) {
            return null;
        }

        deck.fillWithDummies();

        return deck;
    }

    private static void addLands(Deck deck) {

        try (Reader reader = new FileReader(Settings.LANDS_FILE_NAME)) {
            LandCSVParser parser = new LandCSVParser(reader);

            deck.addAll(parser.readAll());
        } catch (IOException e) {
            System.err.println("Could not read csv file: " + e.getMessage());
        } catch (CvsParseException e) {
            System.err.println("Error parsing csv file: " + e.getCause());
        }
    }

    private static void addSpells(Deck deck) {

        try (Reader reader = new FileReader(Settings.SPELLS_FILE_NAME)) {
            SpellCSVParser parse = new SpellCSVParser(reader);

            deck.addAll(parse.readAll());
        } catch (IOException e) {
            System.err.println("Could not read csv file: " + e.getMessage());
        } catch (CvsParseException e) {
            System.err.println("Error parsing csv file: " + e.getCause());
        }
    }

}
