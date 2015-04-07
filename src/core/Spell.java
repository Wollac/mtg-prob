package core;


public class Spell implements Card {

	private final String _name;
	private final ManaCost _cost;

	public Spell(String name, String costString) {
		_name = name;
		_cost = new ManaCost(costString);
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
