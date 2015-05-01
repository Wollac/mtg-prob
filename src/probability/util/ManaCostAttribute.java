package probability.util;

import probability.core.ManaCost;

public class ManaCostAttribute extends Attribute<ManaCost> {

	public ManaCostAttribute(String name) {
		super(name, ManaCost.class, null);
	}

	@Override
	public ManaCost parseValue(String valueString) {
		ManaCost result;

		try {
			result = new ManaCost(valueString);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(valueString + " is not a valid"
					+ " mana cost for attribute " + getName());
		}

		return result;
	}

}
