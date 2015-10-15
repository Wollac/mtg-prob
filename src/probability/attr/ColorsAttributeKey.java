package probability.attr;

import java.util.function.Predicate;

import probability.core.Color;
import probability.core.Colors;

public class ColorsAttributeKey extends AttributeKey<Colors> {

	public ColorsAttributeKey(String name, Predicate<Colors> validator) {
		super(name, Colors.class, new Colors(Color.Colorless), validator);
	}

	public ColorsAttributeKey(String name) {
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
