package probability.core;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class IdentifiedCardObject implements Supplier<Card> {

    private final Card _card;

    private final Card.CardType type;

    private boolean _played;

    private IdentifiedCardObject(Card card) {

        _card = card;
        type = card.getCardType();
    }

    public static List<IdentifiedCardObject> toCardObjects(Collection<? extends Card> cards) {

        return cards.stream().map(IdentifiedCardObject::new).collect(Collectors.toList());
    }

    public Card.CardType getType() {
        return type;
    }

    public boolean isLand() {
        return type == Card.CardType.Land;
    }

    public boolean isSpell() {
        return type == Card.CardType.Spell;
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
