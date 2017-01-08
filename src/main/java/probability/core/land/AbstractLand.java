package probability.core.land;

import static com.google.common.base.Preconditions.checkNotNull;

import probability.core.Color;
import probability.core.Colors;

import java.util.Objects;
import java.util.Set;

abstract class AbstractLand implements Land {

  private final String _name;

  private final Colors _colors;

  // the object is immutable and only used in hash based collections
  private final int _hashCode;

  AbstractLand(String name, Colors colors) {

    _name = checkNotNull(name);
    _colors = checkNotNull(colors);

    _hashCode = Objects.hash(_name, _colors);
  }

  @Override public final String getName() {
    return _name;
  }

  @Override public final CardType getCardType() {
    return CardType.Land;
  }

  @Override public final Set<Color> colors() {
    return _colors.getColors();
  }

  @Override public Set<Color> producibleColors() {
    return colors();
  }

  @Override public Iterable<Color> producesColors() {
    return colors();
  }

  @Override public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    AbstractLand other = (AbstractLand) obj;
    return Objects.equals(_name, other._name) && Objects.equals(_colors, other._colors);
  }

  @Override public int hashCode() {
    return _hashCode;
  }

  @Override public String toString() {
    return getName();
  }

}
