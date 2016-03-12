package probability.rules;

import probability.attr.ImmutableAttributeHolder;

/**
 * Operator for the not-equality of a variable and a value.
 */
class NotEqual extends VariableValueOperator {

    NotEqual() {

        super("<>", 7);
    }

    @Override
    public NotEqual getInstance() {

        return new NotEqual();
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {

        return _variable.equals(_value, bindings);
    }

}
