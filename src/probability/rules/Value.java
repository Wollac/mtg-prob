package probability.rules;

import java.util.Objects;

import probability.attr.ImmutableAttributeHolder;

class Value<T> extends UnparsableToken implements Expression, Token {

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
    public TokenType getTokenType() {
        return TokenType.VALUE;
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {
        return true;
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

        return _type.equals(other._type) && Objects.equals(_value, other._value);
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
