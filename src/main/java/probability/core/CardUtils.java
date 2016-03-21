package probability.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;

import probability.core.land.BasicLand;
import probability.core.land.FetchLand;
import probability.core.land.Land;
import probability.core.land.ReflectingLand;

public final class CardUtils {

    private static final Card DUMMY_CARD = new Card() {

        @Override
        public String getName() {
            return "Dummy";
        }

        @Override
        public CardType getCardType() {
            return CardType.Other;
        }

        @Override
        public String toString() {
            return getName();
        }

    };

    private CardUtils() {
    }

    public static Card getDummyCard() {
        return DUMMY_CARD;
    }

    public static boolean isSpell(Card card) {
        return (card instanceof Spell);
    }

    public static boolean isLand(Card card) {
        return (card instanceof Land);
    }

    public static boolean isDummy(Card card) {
        return card.equals(DUMMY_CARD);
    }

    public static boolean isBasicLand(Card card) {
        return (card instanceof BasicLand);
    }

    public static boolean isFetchLand(Card card) {
        return (card instanceof FetchLand);
    }

    public static boolean isChangingLand(Card card) {
        return card instanceof ReflectingLand;
    }

    public static void retainAllLands(Collection<Card> cards) {
        retain(cards, CardUtils::isLand);
    }

    public static Collection<Land> retainAllLandsToArrayList(
            Collection<Card> cards) {

        Collection<Land> lands = new ArrayList<>(cards.size());

        for (Card card : cards) {
            if (card instanceof Land) {
                lands.add((Land) card);
            }
        }

        return lands;
    }

    public static void retainAllSpells(Collection<Card> cards) {
        retain(cards, CardUtils::isSpell);
    }

    public static Collection<Spell> retainAllSpellsToArrayList(
            Collection<Card> cards) {

        Collection<Spell> spells = new ArrayList<>(cards.size());

        for (Card card : cards) {
            if (card instanceof Spell) {
                spells.add((Spell) card);
            }
        }

        return spells;
    }

    public static Colors getColors(Collection<? extends Land> lands) {

        Set<Color> colorSet = Color.emptyEnumSet();

        for (Land land : lands) {
            colorSet.addAll(land.colors());
        }

        return new Colors(colorSet);
    }

    /**
     * Create a set that contains the cards sorted by getName()
     */
    public static SortedSet<Card> sortCardsByName(Collection<Card> cards) {
        SortedSet<Card> sorted = new TreeSet<>(
                Comparator.comparing(Card::getName));
        sorted.addAll(cards);

        return sorted;
    }

    /**
     * Remove all elements from Card collection that don't fulfill the predicate
     */
    private static void retain(Collection<Card> cards, Predicate<Card> predicate) {

        for (Iterator<Card> iterator = cards.iterator(); iterator.hasNext(); ) {
            Card card = iterator.next();

            if (!predicate.test(card)) {
                iterator.remove();
            }
        }
    }

}
