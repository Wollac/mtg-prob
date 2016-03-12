package probability.rules;

import java.util.Objects;
import java.util.Stack;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A value is a special {@linkplain Token} representing a constant in the rule.
 *
 * @param <T> actual type of the value
 */
class Value<T> implements Token {

    private final T _value;

    private final Class<T> _type;

    public Value(T value, Variable<T> variable) {
        this(value, variable.getType());
    }

    private Value(T value, Class<T> type) {

        _value = value;
        _type = checkNotNull(type);
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
    public Expression parse(Stack<Token> stack) throws RulesTokenException {

        throw new RulesTokenException("Values cannot be used as an expression");
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
