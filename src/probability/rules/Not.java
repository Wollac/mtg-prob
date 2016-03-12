package probability.rules;


import probability.attr.ImmutableAttributeHolder;

class Not extends UnaryOperator {

    Not() {

        super("NOT");
    }

    @Override
    public Operator getInstance() {

        return new Not();
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {

        return !_operand.interpret(bindings);
    }

}
