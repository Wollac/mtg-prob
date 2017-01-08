package probability.core;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

public class Spell implements Card {

  private final String _name;

  private final ManaCost _cost;

  // the object is immutable and only used in hash based collections
  private final int _hashCode;

  public Spell(String name, ManaCost cost) {

    _name = checkNotNull(name);
    _cost = checkNotNull(cost);

    _hashCode = Objects.hash(_name, _cost);
  }

  @Override public String getName() {
    return _name;
  }

  @Override public CardType getCardType() {
    return CardType.Spell;
  }

  public ManaCost getCost() {
    return _cost;
  }

  public int getCMC() {
    return _cost.getConverted();
  }

  @Override public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Spell other = (Spell) obj;
    return Objects.equals(_name, other._name) && Objects.equals(_cost, other._cost);
  }

  @Override public int hashCode() {
    return _hashCode;
  }

  @Override public String toString() {
    return _name + ":" + _cost;
  }
}
