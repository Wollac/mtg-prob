package probability.core.land;

import java.util.Objects;
import java.util.Set;

import probability.core.Board;
import probability.core.Color;
import probability.core.Colors;

abstract class AbstractLand implements Land {

    final private String _name;

    final private Colors _colors;

    private volatile int _hashCode;

    AbstractLand(String name, Colors colors) {
        _name = name;
        _colors = colors;
    }

    @Override
    final public String getName() {
        return _name;
    }

    @Override
    final public CardType getCardType() {
        return CardType.Land;
    }

    @Override
    final public Set<Color> colors() {
        return _colors.getColors();
    }

    @Override
    public Set<Color> producibleColors() {
        return colors();
    }

    @Override
    public Set<Color> producesColors(Board board) {
        return colors();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AbstractLand)) {
            return false;
        }

        AbstractLand other = (AbstractLand) obj;

        // comparing getClass is bad this should go in each super class
        return Objects.equals(getClass(), other.getClass())
                && Objects.equals(_name, other._name)
                && Objects.equals(_colors, other._colors);
    }

    @Override
    public int hashCode() {

        int result = _hashCode;

        if (result == 0) {
            result = Objects.hash(getClass(), _name, _colors);
            _hashCode = result;
        }

        return result;
    }

    @Override
    public String toString() {
        return getName();
    }

}
