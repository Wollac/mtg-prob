package probability.util;

import java.util.function.Predicate;

public class BooleanAttribute extends Attribute<Boolean> {

	public BooleanAttribute(String name, boolean defaultValue,
			Predicate<Boolean> validator) {
		super(name, Boolean.class, defaultValue, validator);
	}

	public BooleanAttribute(String name, boolean defaultValue) {
		super(name, Boolean.class, defaultValue);
	}

	public BooleanAttribute(String name) {
		super(name, Boolean.class, false);
	}

	@Override
	public Boolean parseValue(String valueString) {

		if (valueString.equalsIgnoreCase("true")) {
			return Boolean.TRUE;
		}
		if (valueString.equalsIgnoreCase("false")) {
			return Boolean.FALSE;
		}

		throw new IllegalArgumentException(valueString + " is not a valid"
				+ " boolean for attribute " + getName());
	}

}
