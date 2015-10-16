package probability.rules;

import java.util.Objects;
import java.util.Stack;

import probability.attr.AttributeKey;
import probability.attr.AttributeKey.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;

class Variable<T> implements Expression {

  private AttributeKey<T> _key;

  private Variable(AttributeKey<T> key) {

    _key = key;
  }

  public AttributeKey<T> getAttributeKey() {

    return _key;
  }

  public Class<T> getType() {

    return _key.getValueType();
  }

  public String getTypeName() {

    return getType().getSimpleName();
  }

  public Value<T> createParsedValue(Value<String> stringValue) throws AttributeParseException {

    return createParsedValue(stringValue.getValue());
  }

  public Value<T> createParsedValue(String valueString) throws AttributeParseException {

    return new Value<>(_key.parseValue(valueString), this);
  }

  public static <T> Variable<T> createVariable(AttributeKey<T> key) {

    return new Variable<>(key);
  }

  @Override
  public boolean interpret(ImmutableAttributeHolder bindings) {

    throw new IllegalStateException();
  }

  @Override
  public void parse(Stack<Expression> stack) {

    throw new IllegalArgumentException("The variable " + _key.getName() + " cannot be evaluated");
  }

  public T getValue(ImmutableAttributeHolder bindings) {

    return bindings.getAttributeValue(_key);
  }

  public boolean equals(Value<?> value, ImmutableAttributeHolder bindings) {

    checkType(value);

    return Objects.equals(getValue(bindings), value.getValue());
  }

  public int compareTo(Value<?> other, ImmutableAttributeHolder bindings) {

    checkType(other);

    if (Comparable.class.isAssignableFrom(getType())) {

      Comparable comparable = (Comparable) getValue(bindings);

      return comparable.compareTo(other.getValue());
    }
    throw new IllegalStateException("Cannot compare a variable of type " + getTypeName());
  }

  private void checkType(Value<?> value) {

    if (!getType().equals(value.getType())) {
      throw new IllegalStateException("Cannot compare variable " + _key.getName() + " of type "
          + _key.getValueType() + " with a value of type " + getTypeName());
    }
  }

  public static <T extends Comparable<T>> int compare(T x, T y) {

    return x.compareTo(y);
  }

}
