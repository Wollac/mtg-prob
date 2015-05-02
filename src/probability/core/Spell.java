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
		return _cost.getCMC();
	}

	@Override
	public String toString() {
		return _name + ":" + _cost;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_name, _cost);
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Spell)) {
			return false;
		}

		final Spell other = (Spell) obj;

		return Objects.equals(_name, other._name)
				&& Objects.equals(_cost, other._cost);
	}

}
