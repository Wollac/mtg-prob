package probability.csv;

public abstract class Attribute<T> {

	final private String _name;

	final private T _defaultValue;

	final private Class<T> _type;

	public Attribute(String name, Class<T> type, T defaultValue) {
		_name = name;
		_defaultValue = defaultValue;
		_type = type;
	}

	public final String getName() {
		return _name;
	}

	public final T getDefaultValue() {
		return _defaultValue;
	}

	public final Class<T> getValueType() {
		return _type;
	}

	public abstract T parseValue(String valueString);

	@Override
	public String toString() {
		return getName();
	}

}
