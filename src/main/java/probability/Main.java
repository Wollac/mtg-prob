package probability;

import org.pmw.tinylog.Logger;
import probability.checker.PlayableChecker;
import probability.config.Settings;
import probability.core.CardUtils;
import probability.core.Deck;
import probability.core.MulliganRule;
import probability.core.Spell;
import probability.core.land.Land;
import probability.csv.LandsReader;
import probability.csv.SpellsReader;
import probability.messages.Messages;
import probability.messages.ProjectException;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Main {

  public static void main(String[] args) {

    try {
      run();
    } catch (ProjectException exception) {

      Logger.error(exception.getLocalizedMessage());

      Throwable cause = exception.getCause();
      if (cause != null) {
        Logger.debug(cause, "cause");
      }
    }
  }

  private static void run() throws ProjectException {

    Deck deck = buildDeck();

    MulliganRule mulliganRule = new MulliganRule(new File(Settings.MULLIGAN_RULES_FILE_NAME));

    System.out.println(deck.toFormattedString());

    System.out.println(Messages.get().takeMulligan());
    System.out.println(mulliganRule.toFormattedString());

    Set<Integer> convertedManaCosts = CardUtils.getConvertedManaCosts(deck.cards());

    final int minCmc = Collections.min(convertedManaCosts);
    final int maxCmc = Collections.min(convertedManaCosts);

    System.out.println(Messages.get().combinedFailureProbability());

    PlayableChecker checker = new PlayableChecker(deck, mulliganRule);

    for (int turn = minCmc; turn <= maxCmc + Settings.config.turnsAfterMaxCmc(); turn++) {

      final int playable = checker.countPlayable(turn);
      final double factor = 1.0 - (double) playable / Settings.config.sampleSize();

      System.out.println(Messages.get().probability(turn, factor));
    }
  }

  private static Deck buildDeck() throws ProjectException {

    Logger.debug("Building deck");

    Deck deck = new Deck();

    addLands(deck);
    addSpells(deck);

    deck.validate();

    deck.fillWithDummies();

    return deck;
  }

  private static void addLands(Deck deck) {

    LandsReader reader = new LandsReader(Settings.LANDS_FILE_NAME);

    List<Land> lands = reader.read();
    Logger.debug("{} lands read", lands.size());

    deck.addAll(lands);
  }

  private static void addSpells(Deck deck) {

    SpellsReader reader = new SpellsReader(Settings.SPELLS_FILE_NAME);

    List<Spell> spells = reader.read();
    Logger.debug("{} spells read", spells.size());

    deck.addAll(spells);
  }

}
