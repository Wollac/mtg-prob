package probability.util;

import java.util.function.Predicate;

public class IntegerAttribute extends Attribute<Integer> {

	public IntegerAttribute(String name, int defaultValue,
			Predicate<Integer> validator) {
		super(name, Integer.class, defaultValue, validator);
	}

	public IntegerAttribute(String name, int defaultValue) {
		super(name, Integer.class, defaultValue);
	}

	public IntegerAttribute(String name) {
		super(name, Integer.class, 0);
	}

	@Override
	public Integer parseValue(String valueString)
			throws AttributeParseException {

		Integer result;

		try {
			result = Integer.valueOf(valueString);
		} catch (NumberFormatException e) {
			throw new AttributeParseException(valueString
					+ " is not a valid string", this);
		}

		return result;
	}

}
