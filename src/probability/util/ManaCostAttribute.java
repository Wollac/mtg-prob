package probability.util;

import probability.core.ManaCost;

public class ManaCostAttribute extends Attribute<ManaCost> {

	public ManaCostAttribute(String name) {
		super(name, ManaCost.class, null);
	}

	@Override
	public ManaCost parseValue(String valueString)
			throws AttributeParseException {

		ManaCost result;

		try {
			result = new ManaCost(valueString);
		} catch (IllegalArgumentException e) {
			throw new AttributeParseException(valueString
					+ " is not a valid string", this);
		}

		return result;
	}

}
