package probability.checker;

import probability.core.Card;
import probability.core.land.Land;

public final class PlayableLand implements Card {

    private final Land _land;

    private boolean _played;

    public PlayableLand(Land land) {
        _land = land;
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

    public Land getLand() {
        return _land;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PlayableLand && ((PlayableLand) obj)._land.equals(_land);
    }

    @Override
    public int hashCode() {
        return _land.hashCode();
    }

    @Override
    public String getName() {
        return _land.getName();
    }

    @Override
    public CardType getCardType() {
        return _land.getCardType();
    }
}
