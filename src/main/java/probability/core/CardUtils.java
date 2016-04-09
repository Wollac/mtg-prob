package probability.core;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import probability.core.land.Land;

final class CardUtils {

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
     * Create a set that contains the cards sorted by getName()
     */
    static SortedSet<Card> sortCardsByName(Collection<Card> cards) {

        SortedSet<Card> sorted = new TreeSet<>(
                Comparator.comparing(Card::getName));
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

        int n = 0;
        for (CardObject o : cardObjects) {
            if (o.isLand()) {
                n++;
            }
        }

        return n;
    }

}
