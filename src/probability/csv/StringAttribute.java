package probability.csv;

public class StringAttribute extends Attribute<String> {

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
