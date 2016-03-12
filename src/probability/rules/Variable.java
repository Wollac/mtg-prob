package probability.rules;

import probability.attr.AttributeKey;
import probability.attr.AttributeKey.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;

import java.util.Objects;

class Variable<T> extends UnparsableToken implements Expression, Token {

    private final AttributeKey<T> _key;

    private Variable(AttributeKey<T> key) {

        _key = key;
    }

    public static <T> Variable<T> createVariable(AttributeKey<T> key) {

        return new Variable<>(key);
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

    T parseValue(Value<String> stringValue) throws RulesTokenException {
        try {
            return _key.parseValue(stringValue.getValue());
        } catch (AttributeParseException e) {
            throw new RulesTokenException(e.getMessage());
        }
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.VALUE;
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {

        if (!getType().equals(Boolean.class)) {

            throw new IllegalStateException("only boolean variables can be" +
                    " evaluated directly");
        }

        return (Boolean) getValue(bindings);
    }

    T getValue(ImmutableAttributeHolder bindings) {

        return bindings.getAttributeValue(_key);
    }

    public boolean equals(Object other, ImmutableAttributeHolder bindings) {

        return Objects.equals(getValue(bindings), other);
    }

    public int compareTo(Object other, ImmutableAttributeHolder bindings) {

        if (Comparable.class.isAssignableFrom(getType())) {

            // This cast is correct, as getType() gives aus the class type of the value and due to type
            // erasure the generic type of Comparable becomes irrelevant
            Comparable<Object> comparable = (Comparable<Object>) getValue(bindings);

            return comparable.compareTo(other);
        }
        throw new IllegalStateException("Cannot compare a variable of type " + getTypeName());
    }

    @Override
    public String toString() {
        return _key.getName();
    }

}
