package probability.attr;

import java.util.HashMap;
import java.util.Map;

public final class AttributeHolder implements ImmutableAttributeHolder {

    private final Map<AttributeKey<?>, Object> _map;

    public AttributeHolder() {
        _map = new HashMap<>();
    }

    public <T> void setAttributeValue(AttributeKey<T> key, T value) {

        try {
            key.checkValid(value);
        } catch (AttributeParseException e) {
            throw new IllegalArgumentException(e);
        }

        setAttributeValueUnchecked(key, value);
    }

    public <T> void setAttributeValueUnchecked(AttributeKey<T> key, T value) {

        _map.put(key, value);
    }

    public <T> void setParsedAttributeValue(AttributeKey<T> key, String valueString)
            throws AttributeParseException {

        T value = key.parseValue(valueString);

        if (!key.isValid(value)) {
            throw new AttributeParseException(AttributeParseException.AttributeParseError.INVALID_VALUE, key);
        }

        setAttributeValueUnchecked(key, value);
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

}
