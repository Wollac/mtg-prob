package probability.rules;

import probability.attr.AttributeKey;
import probability.attr.Class;
import probability.attr.AttributeKey.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;

public class Variable<T> implements Expression, ValueProvider<T> {

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

    public VariableValue<T> createParsedVariableValue(String valueString)
        throws AttributeParseException {

        return new VariableValue<>(_key.parseValue(valueString));
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

        throw new IllegalArgumentException("The variable " + _key.getName()
            + " cannot be evaluated");
    }

    @Override
    public T getValue(ImmutableAttributeHolder bindings) {

        return bindings.getAttributeValue(_key);
    }

    public boolean equals(VariableValue other, ImmutableAttributeHolder bindings) {

        return Objects.equals(getValue(bindings), other.getValue());
    }

    public int compareTo(VariableValue other, ImmutableAttributeHolder bindings) {

        if(Comparable.class.isAssignableFrom(_key.getValueType())) {

            Comparable comparable = (Comparable) getValue(bindings);
            return comparable.compareTo(other.getValue());
        }
        throw new IllegalStateException("Cannot compare a variable of type "
            + _key.getValueType());
    }

    public class VariableValue implements Expression, ValueProvider<T> {

        private T _value;

        private VariableValue(T value) {

            this._value = value;
        }

        public T getValue() {
            _value;
        }

        @Override
        public boolean interpret(ImmutableAttributeHolder bindings) {

            throw new IllegalStateException();
        }

        @Override
        public void parse(Stack<Expression> stack) {

            throw new IllegalArgumentException("The value " + _value
                + " of variable " + _key.getName() + " cannot be evaluated");
        }

        @Override
        public T getValue(ImmutableAttributeHolder bindings) {

            return getValue();
        }

    }

}
