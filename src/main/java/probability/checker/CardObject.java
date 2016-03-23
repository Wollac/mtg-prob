package probability.checker;

import java.util.function.Supplier;

import probability.core.Card;

final class CardObject implements Supplier<Card> {

    private final Card _card;

    private boolean _played;

    public CardObject(Card card) {
        _card = card;
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

    @Override
    public Card get() {
        return _card;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CardObject && ((CardObject) obj)._card.equals(_card);
    }

    @Override
    public int hashCode() {
        return _card.hashCode();
    }

    @Override
    public String toString() {
        return _card.toString();
    }
}
