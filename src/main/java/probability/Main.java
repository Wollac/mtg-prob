package probability;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import probability.checker.PlayableChecker;
import probability.config.Settings;
import probability.core.CardUtils;
import probability.core.Deck;
import probability.core.MulliganRule;
import probability.csv.LandsReader;
import probability.csv.SpellsReader;
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

        LandsReader reader = new LandsReader(Settings.LANDS_FILE_NAME);
        deck.addAll(reader.read());
    }

    private static void addSpells(Deck deck) {

        SpellsReader reader = new SpellsReader(Settings.SPELLS_FILE_NAME);
        deck.addAll(reader.read());
    }

}
