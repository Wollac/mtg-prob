package probability.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    public static Card getDummyCard() {
        return DUMMY_CARD;
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

    public static Set<String> getNames(Collection<IdentifiedCardObject> cardObjects) {

        Set<String> names = new HashSet<>();

        for (IdentifiedCardObject o : cardObjects) {

            if (o.getType() == Card.CardType.Other) {
                names.add(o.getName());
            }
        }

        return names;
    }

    public static Collection<Land> retainAllLandsToArrayList(
            Collection<IdentifiedCardObject> cardObjects) {

        if (cardObjects.isEmpty()) {
            return Collections.emptyList();
        }

        Collection<Land> lands = new ArrayList<>();
        for (IdentifiedCardObject o : cardObjects) {
            if (o.isLand()) {
                lands.add((Land) o.get());
            }
        }

        return lands;
    }
}
