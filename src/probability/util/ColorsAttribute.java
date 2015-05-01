package probability.util;

import java.util.function.Predicate;

import probability.core.Color;
import probability.core.Colors;

public class ColorsAttribute extends Attribute<Colors> {

	public ColorsAttribute(String name, Predicate<Colors> validator) {
		super(name, Colors.class, new Colors(Color.Colorless), validator);
	}

	public ColorsAttribute(String name) {
		this(name, s -> true);
	}

	@Override
	public Colors parseValue(String valueString)
			throws AttributeParseException {

		Colors result;

		try {
			result = Colors.valueOf(valueString);
		} catch (IllegalArgumentException e) {
			throw new AttributeParseException(valueString
					+ " is not a valid string", this);
		}

		return result;
	}
}
