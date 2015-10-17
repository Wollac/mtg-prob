package probability.main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import probability.config.AbstractConfigLoader.ConfigParseException;
import probability.config.Config;
import probability.config.ConfigLoader;
import probability.core.Card;
import probability.core.Deck;
import probability.core.Hand;
import probability.core.MulliganRule;
import probability.core.Spell;
import probability.csv.AbstractCSVParser.CvsParseException;
import probability.csv.LandCSVParser;
import probability.csv.SpellCSVParser;

public class Main {

  private static final ConfigLoader config = new ConfigLoader();

  public static void main(String[] args) {

    // config.write(new File("mtg.config"));

    try {
      config.load(new File("mtg.config"));
    } catch (IOException e) {
      System.err.println("Could not read config file: " + e.getMessage());
      return;
    } catch (ConfigParseException e) {
      System.err.println("Error parsing config file: " + e.getCause());
      return;
    }

    MulliganRule mulliganRule = new MulliganRule();

    Deck deck = buildDeck(config);

    System.out.println("The following deck has been loaded:");
    System.out.println(deck.toFormatedString());

    Set<Integer> cmcs = getCmcs(deck);

    int minCmc = Collections.min(cmcs);
    int maxCmc = Collections.min(cmcs);

    System.out.println("Calculating the combined failure probability for" + " the given spells:");

    for (int turn = minCmc; turn <= maxCmc + 3; turn++) {

      int playable = countPlayable(deck, mulliganRule, turn);
      double factor = 1.0 - (double) playable / config.sampleSize();

      System.out.printf("Turn %02d: %4.1f%%%n", turn, factor * 100.0);
    }

  }

  private static Set<Integer> getCmcs(Deck deck) {

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

    deck.fillWithDummies();

    return deck;
  }

  private static void addLands(Deck deck) {

    try {
      LandCSVParser parser = new LandCSVParser(new FileReader("lands.csv"));

      deck.addAll(parser.readAll());
    } catch (IOException e) {
      System.err.println("Could not read csv file: " + e.getMessage());
    } catch (CvsParseException e) {
      System.err.println("Error parsing csv file: " + e.getCause());
    }
  }

  private static void addSpells(Deck deck) {

    try {
      SpellCSVParser parse = new SpellCSVParser(new FileReader("spells.csv"));

      deck.addAll(parse.readAll());
    } catch (IOException e) {
      System.err.println("Could not read csv file: " + e.getMessage());
    } catch (CvsParseException e) {
      System.err.println("Error parsing csv file: " + e.getCause());
    }
  }

  private static int countPlayable(Deck deck, MulliganRule mulliganRule, int turn) {
    int good = 0;

    for (int i = 0; i < config.sampleSize(); i++) {

      Hand hand = getStartingHand(deck, mulliganRule, turn);

      PlayableChecker checker = new PlayableChecker(deck, hand);

      if (checker.isPlayable(turn)) {
        good++;
      }
    }

    return good;
  }

  private static Hand getStartingHand(Deck deck, MulliganRule mulliganRule, int turn) {
    Hand hand = null;
    for (int mulligan = 0; mulligan < config.initialHandSize(); mulligan++) {

      deck.shuffle();
      hand = deck.draw(turn, mulligan);

      if (!mulliganRule.takeMulligan(hand.getNumerOfLandsInStartingHand(),
          hand.getStartingHandSize())) {
        break;
      }

      mulligan++;
    }

    return hand;
  }

}
