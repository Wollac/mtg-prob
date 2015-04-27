package probability.core;

public class Spell implements Card {

	private final String _name;
	private final ManaCost _cost;

	public Spell(String name, ManaCost cost) {
		_name = name;
		_cost = cost;
	}

	public String getName() {
		return _name;
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

}
