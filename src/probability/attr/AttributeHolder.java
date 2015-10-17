package probability.attr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import probability.attr.AttributeKey.AttributeParseException;

public class AttributeHolder implements ImmutableAttributeHolder {

  private final Map<AttributeKey<?>, Object> _map;

  public AttributeHolder() {
    _map = new HashMap<>();
  }

  public <T> void setAttributeValue(AttributeKey<T> key, T value) {
    
    key.checkValid(value);
    _map.put(key, value);
  }
  
  public <T> void setAttributeValueUnchecked(AttributeKey<T> key, T value) {

    _map.put(key, value);
  }

  public <T> void setParsedAttributeValue(AttributeKey<T> attribute, String valueString)
      throws AttributeParseException {

    T value = attribute.parseValue(valueString);

    if (!attribute.isValid(value)) {
      throw new AttributeParseException(value + " is not a valid value", attribute);
    }

    attribute.checkValid(value);

    _map.put(attribute, value);
  }

  @Override
  public <T> T getAttributeValue(AttributeKey<T> key, T def) {
    Object valueObject = _map.get(key);

    if (valueObject == null) {
      return def != null ? def : key.getDefaultValue();
    }

    return key.getValueType().cast(valueObject);
  }

  @Override
  public <T> T getAttributeValue(AttributeKey<T> key) {
    return getAttributeValue(key, null);
  }

  public Set<AttributeKey<?>> getAttributeKeys() {
    return Collections.unmodifiableSet(_map.keySet());
  }

}
