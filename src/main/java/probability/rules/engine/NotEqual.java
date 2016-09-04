package probability.rules.engine;

import probability.attr.ImmutableAttributeHolder;

/**
 * Operator for the not-equality of a variable and a value.
 */
class NotEqual extends VariableValueOperator {

    NotEqual() {

        super("<>", 7);
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {

        return !_variable.equals(_value, bindings);
    }

}
