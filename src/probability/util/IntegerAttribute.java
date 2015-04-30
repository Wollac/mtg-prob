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
	public Integer parseValue(String valueString) {
		Integer result;

		try {
			result = Integer.valueOf(valueString);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(valueString
					+ " is not a valid integer");
		}
		
		checkValid(result);

		return result;
	}

}
