package probability.core;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A card object represents a current instance of a card that can be played or not. There can very
 * well be multiple card objects corresponding to the same card (type). As each instance represents
 * it own object they should be collected in a {@code List} and not a {@code Set}.
 */
public final class CardObject implements Supplier<Card> {

    private final Card _card;

    // for some reason caching the type and then comparing is considerably faster than instanceof
    private final Card.CardType type;

    private boolean _played;

    /**
     * Creates a card object corresponding to the given card.
     */
    private CardObject(Card card) {

        _card = card;
        type = card.getCardType();
    }

    /**
     * Creates a new card object of each of the provided cards and returns them as a list.
     */
    public static List<CardObject> toCardObjects(Collection<? extends Card> cards) {

        return cards.stream().map(CardObject::new).collect(Collectors.toList());
    }

    public Card.CardType getType() {
        return type;
    }

    public boolean isLand() {
        return getType() == Card.CardType.Land;
    }

    public boolean isSpell() {
        return getType() == Card.CardType.Spell;
    }

    public boolean isOther() {
        return getType() == Card.CardType.Other;
    }

    public String getName() {
        return _card.getName();
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
    public String toString() {
        return _card.toString();
    }

}
