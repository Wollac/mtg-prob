package probability.rules;

import probability.attr.ImmutableAttributeHolder;

/**
 * Operator for the logical OR, performing short-circuit evaluation.
 */
class Or extends BinaryOperator {

    public Or() {
        super("OR", 12);
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {

        return _leftOperand.interpret(bindings) || _rightOperand.interpret(bindings);
    }

}
