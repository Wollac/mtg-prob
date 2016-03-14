package probability.core;

import java.util.Objects;

public class Spell implements Card {

    private final String _name;

    private final ManaCost _cost;

    public Spell(String name, ManaCost cost) {
        _name = name;
        _cost = cost;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public CardType getCardType() {
        return CardType.Spell;
    }

    public ManaCost getCost() {
        return _cost;
    }

    public int getCMC() {
        return _cost.getConverted();
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, _cost);
    }

    @Override
    public final boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Spell)) {
            return false;
        }

        Spell other = (Spell) obj;

        return Objects.equals(_name, other._name)
                && Objects.equals(_cost, other._cost);
    }

    @Override
    public String toString() {
        return _name + ":" + _cost;
    }
}
