package probability.attr;

import java.util.HashMap;
import java.util.Map;

/**
 * Container for several attributes that can be changed by specifying the new value directly or by
 * parsing a value string.
 */
public final class ParsingAttributeHolder implements AttributeHolder {

    private final Map<AttributeKey<?>, Object> _map;

    public ParsingAttributeHolder() {
        _map = new HashMap<>();
    }

    @Override
    public <T> void setAttributeValue(AttributeKey<T> key, T value) {

        try {
            key.checkValid(value);
        } catch (AttributeParseException e) {
            throw new IllegalArgumentException(e);
        }

        setAttributeValueUnchecked(key, value);
    }

    @Override
    public <T> void setAttributeValueUnchecked(AttributeKey<T> key, T value) {

        _map.put(key, value);
    }

    @Override
    public <T> T getAttributeValue(AttributeKey<T> key, T def) {

        Object valueObject = _map.get(key);
        if (valueObject == null) {
            return def;
        }

        @SuppressWarnings("unchecked")
        T value = (T) valueObject;

        return value;
    }

    @Override
    public <T> T getAttributeValue(AttributeKey<T> key) {
        return getAttributeValue(key, key.getDefaultValue());
    }

    /**
     * Sets the value of the corresponding {@link AttributeKey} by parsing the provided string.
     *
     * @param key         the key whose value should be changed
     * @param valueString string representation of the new value
     * @throws AttributeParseException if the string could not be parsed
     */
    public <T> void setParsedAttributeValue(AttributeKey<T> key, String valueString)
            throws AttributeParseException {

        T value = key.parseValue(valueString);

        if (!key.isValid(value)) {
            throw new AttributeParseException(AttributeParseException.AttributeParseError.INVALID_VALUE, key);
        }

        setAttributeValueUnchecked(key, value);
    }
}
