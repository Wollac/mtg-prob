package probability.rules;

import probability.attr.ImmutableAttributeHolder;

class Or extends BinaryOperator {

    public Or() {
        super("OR", 12);
    }

    @Override
    public Or getInstance() {
        return new Or();
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {

        return _leftOperand.interpret(bindings) || _rightOperand.interpret(bindings);
    }

}
