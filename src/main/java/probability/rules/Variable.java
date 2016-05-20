package probability.rules;

import java.util.Objects;
import java.util.Stack;

import probability.attr.AttributeKey;
import probability.attr.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;

import static com.google.common.base.Preconditions.checkNotNull;

class Variable<T> implements Expression, Token {

    private final AttributeKey<T> _key;

    Variable(AttributeKey<T> key) {

        _key = checkNotNull(key);
    }

    public static <T> Variable<T> createVariable(AttributeKey<T> key) {

        return new Variable<>(key);
    }

    public AttributeKey<T> getAttributeKey() {

        return _key;
    }

    public Class<? super T> getType() {

        return _key.getValueType();
    }

    public String getTypeName() {

        return _key.getValueType().toString();
    }

    T parseValue(Value<String> stringValue) throws RulesTokenException {
        try {
            return _key.parseValue(stringValue.getValue());
        } catch (AttributeParseException e) {
            throw new RulesTokenException(e.getMessage());
        }
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
            @SuppressWarnings("unchecked")
            Comparable<Object> comparable = (Comparable<Object>) getValue(bindings);

            return comparable.compareTo(other);
        }
        throw new IllegalStateException("Cannot compare a variable of type " + getTypeName());
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {

        if (!getType().equals(Boolean.class)) {

            throw new IllegalStateException("only boolean variables can be" +
                    " evaluated directly");
        }

        return (Boolean) getValue(bindings);
    }

    @Override
    public Expression parse(Stack<Token> stack) throws RulesTokenException {

        if (!getType().equals(Boolean.class)) {

            throw new RulesTokenException("Only Boolean-Variables can be used as an expression");
        }

        return this;
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.VALUE;
    }

    @Override
    public String toString() {
        return _key.getName();
    }

}
