package probability.util;

import probability.core.ManaCost;

public class ManaCostAttribute extends Attribute<ManaCost> {

	public ManaCostAttribute(String name) {
		super(name, ManaCost.class, null);
	}

	@Override
	public ManaCost parseValue(String valueString) {
		return new ManaCost(valueString);
	}

}
