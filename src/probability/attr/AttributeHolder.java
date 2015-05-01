package probability.attr;

import java.util.HashMap;
import java.util.Map;

import probability.attr.Attribute.AttributeParseException;

public class AttributeHolder implements ImmutableAttributeHolder {

	private final Map<Attribute<?>, Object> _map;

	public AttributeHolder() {
		_map = new HashMap<>();
	}

	public <T> void setAttributeValue(Attribute<T> key, T value) {
		key.checkValid(value);

		_map.put(key, value);
	}

	public <T> void setParsedAttributeValue(Attribute<T> attribute,
			String valueString) throws AttributeParseException {

		T value = attribute.parseValue(valueString);

		if (!attribute.isValid(value)) {
			throw new AttributeParseException(value + " is not a valid value",
					attribute);
		}

		attribute.checkValid(value);

		_map.put(attribute, value);
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
