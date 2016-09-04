package probability.rules.engine;

import probability.attr.ImmutableAttributeHolder;

/**
 * Operator for the greater-than relation of a variable and a value of comparable type.
 */
class GreaterThan extends RelationalOperator {

    public GreaterThan() {

        super(">");
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {

        return _variable.compareTo(_value, bindings) > 0;
    }

}
