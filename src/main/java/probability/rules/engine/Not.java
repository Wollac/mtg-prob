package probability.rules.engine;

import probability.attr.ImmutableAttributeHolder;

class Not extends UnaryOperator {

    Not() {

        super("NOT");
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {

        return !_operand.interpret(bindings);
    }

}
