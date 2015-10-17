package probability.rules;

import java.util.Objects;
import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;

class Value<T> implements Expression {

  private T _value;

  private Class<T> _type;

  public Value(T value, Variable<T> variable) {
    this(value, variable.getType());
  }

  private Value(T value, Class<T> type) {
    _value = value;
    _type = type;
  }

  public T getValue() {
    return _value;
  }

  public Class<T> getType() {
    return _type;
  }

  @Override
  public boolean interpret(ImmutableAttributeHolder bindings) {
    return true;
  }

  @Override
  public void parse(Stack<Expression> stack) throws AttributeParseException {
    throw new IllegalArgumentException(
        "The value " + _value + " of type " + _type + " cannot be evaluated");
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Value)) {
      return false;
    }

    Value<?> other = (Value<?>) obj;
    if (!_type.equals(other._type)) {
      return false;
    }

    return Objects.equals(_value, other._value);
  }

  @Override
  public String toString() {
    return _value.toString();
  }

  public static class StringValue extends Value<String> {

    public StringValue(String value) {
      super(value, String.class);
    }

  }

}
