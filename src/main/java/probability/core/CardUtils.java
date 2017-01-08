package probability.core;

import probability.core.land.Land;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public final class CardUtils {

  private static final Card DUMMY_CARD = new Card() {

    @Override public String getName() {
      return "Dummy";
    }

    @Override public CardType getCardType() {
      return CardType.Other;
    }

    @Override public String toString() {
      return getName();
    }

  };

  private CardUtils() {
  }

  static Card getDummyCard() {
    return DUMMY_CARD;
  }

  static Colors getLandColors(Collection<CardObject> cardObjects) {

    if (cardObjects.isEmpty()) {
      return Colors.emptyColors();
    }

    Set<Color> colorSet = Color.emptyEnumSet();

    for (CardObject o : cardObjects) {

      if (o.isLand()) {
        colorSet.addAll(((Land) o.get()).colors());
      }
    }

    return new Colors(colorSet);
  }

  /**
   * Create a set that contains the cards sorted by {@code Card::getName}.
   */
  static SortedSet<Card> sortCardsByName(Collection<Card> cards) {

    SortedSet<Card> sorted = new TreeSet<>(Comparator.comparing(Card::getName));
    sorted.addAll(cards);

    return sorted;
  }

  static Set<String> getNames(Iterable<CardObject> cardObjects) {

    Set<String> names = new HashSet<>();

    for (CardObject o : cardObjects) {

      if (!o.isOther()) {
        names.add(o.getName());
      }
    }

    return names;
  }

  static int getNumberOfLandObjects(Iterable<CardObject> cardObjects) {

    int result = 0;
    for (CardObject o : cardObjects) {
      if (o.isLand()) {
        result++;
      }
    }

    return result;
  }

  /** Returns a set containing all the different the converted mana costs of the given cards. */
  public static Set<Integer> getConvertedManaCosts(Iterable<? extends Card> cards) {

    Set<Integer> result = new HashSet<>();
    for (Card c : cards) {
      if (c instanceof Spell) {
        result.add(((Spell) c).getCMC());
      }
    }

    return result;
  }

}
