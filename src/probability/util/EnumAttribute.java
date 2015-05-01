package probability.util;

import java.util.function.Predicate;

public class EnumAttribute<E extends Enum<E>> extends Attribute<E> {

	public EnumAttribute(String name, Class<E> type, E defaultValue,
			Predicate<E> validator) {
		super(name, type, defaultValue, validator);
	}

	public EnumAttribute(String name, Class<E> type, E defaultValue) {
		this(name, type, defaultValue, e -> true);
	}

	@Override
	public E parseValue(String valueString) throws AttributeParseException {
		E result;

		try {
			result = Enum.valueOf(getValueType(), valueString);
		} catch (IllegalArgumentException e) {
			throw new AttributeParseException(valueString
					+ " is not a valid string", this);
		}

		return result;
	}
}