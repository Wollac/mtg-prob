package probability.core.land;

import probability.core.Board;
import probability.core.Color;
import probability.core.Colors;

import java.util.Objects;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

abstract class AbstractLand implements Land {

    private final String _name;

    private final Colors _colors;

    private volatile int _hashCode;

    AbstractLand(String name, Colors colors) {

        _name = checkNotNull(name);
        _colors = checkNotNull(colors);
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
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        AbstractLand other = (AbstractLand) obj;
        return Objects.equals(_name, other._name)
                && Objects.equals(_colors, other._colors);
    }

    @Override
    public int hashCode() {

        int result = _hashCode;

        if (result == 0) {
            result = Objects.hash(_name, _colors);
            _hashCode = result;
        }

        return result;
    }

    @Override
    public String toString() {
        return getName();
    }

}
