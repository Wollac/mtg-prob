package probability.checker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import probability.core.Card;

final class IdentifiedCardObject implements Supplier<Card> {

    private final Card _card;

    private final int _id;

    private boolean _played;

    private IdentifiedCardObject(Card card, int id) {

        _card = card;
        _id = id;
    }

    public static List<IdentifiedCardObject> toCardObjects(Collection<? extends Card> cards) {

        List<IdentifiedCardObject> result = new ArrayList<>();

        int index = 0;
        for (Card card : cards) {
            result.add(new IdentifiedCardObject(card, index++));
        }

        return result;
    }

    public void markPlayed() {
        _played = true;
    }

    public void markNotPlayed() {
        _played = false;
    }

    public boolean isPlayed() {
        return _played;
    }

    public int getId() {
        return _id;
    }

    @Override
    public Card get() {
        return _card;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IdentifiedCardObject && ((IdentifiedCardObject) obj)._id == _id;
    }

    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public String toString() {
        return _card.toString();
    }
}
