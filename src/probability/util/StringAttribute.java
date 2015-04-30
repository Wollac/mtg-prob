package probability.util;

import java.util.function.Predicate;

public class StringAttribute extends Attribute<String> {

	public StringAttribute(String name, String defaultValue,
			Predicate<String> validator) {
		super(name, String.class, defaultValue, validator);
	}

	public StringAttribute(String name, String defaultValue) {
		super(name, String.class, defaultValue);
	}

	public StringAttribute(String name) {
		super(name, String.class, "");
	}

	@Override
	public String parseValue(String valueString) {
		return new String(valueString);
	}

}
