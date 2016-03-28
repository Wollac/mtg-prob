package probability.rules;

import java.util.Objects;
import java.util.Stack;

/**
 * A value is a special {@linkplain Token} representing a constant in the rule.
 *
 * @param <T> actual type of the value
 */
class Value<T> implements Token {

    private final T _value;

    public Value(T value) {
        _value = value;
    }

    public Value(T value, Variable<T> variable) {
        _value = value;
    }

    public T getValue() {
        return _value;
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.VALUE;
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
        return Objects.equals(_value, other._value);
    }

    @Override
    public Expression parse(Stack<Token> stack) throws RulesTokenException {

        throw new RulesTokenException("Values cannot be used as an expression");
    }

    @Override
    public String toString() {
        return _value.toString();
    }

    public static class StringValue extends Value<String> {

        public StringValue(String value) {
            super(value);
        }

        @Override
        public String toString() {

            String value = getValue();

            if (value.indexOf(' ') >= 0) {
                return '"' + value + '"';
            }
            return value;
        }
    }

}
