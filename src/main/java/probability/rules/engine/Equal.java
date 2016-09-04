package probability.rules.engine;

import probability.attr.ImmutableAttributeHolder;

/**
 * Operator for the equality of a variable and a value.
 */
class Equal extends VariableValueOperator {

    Equal() {

        super("=", 7);
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {

        return _variable.equals(_value, bindings);
    }

}
