package probability.util;

import java.util.HashMap;
import java.util.Map;

import probability.csv.Row;

public class AttributeHolder implements Row {

	private final Map<Attribute<?>, Object> _map;

	public AttributeHolder() {
		_map = new HashMap<>();
	}

	public <T> void setAttributeValue(Attribute<T> key, T value) {
		_map.put(key, value);
	}

	public <T> void setParsedAttributeValue(Attribute<T> key, String valueString) {
		_map.put(key, key.parseValue(valueString));
	}

	public <T> T getAttributeValue(Attribute<T> key, T def) {
		Object valueObject = _map.get(key);

		if (valueObject == null) {
			return def != null ? def : key.getDefaultValue();
		}

		return key.getValueType().cast(valueObject);
	}

	public <T> T getAttributeVale(Attribute<T> key) {
		return getAttributeValue(key, null);
	}
}
