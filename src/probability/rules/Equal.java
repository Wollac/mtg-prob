package probability.rules;

import probability.attr.ImmutableAttributeHolder;

class Equal extends VariableValueOperator {

    Equal() {

        super("=", 7);
    }

    @Override
    public Equal getInstance() {

        return new Equal();
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {

        Variable<?> var = (Variable<?>) _leftOperand;
        Value<?> value = (Value<?>) _rightOperand;

        return var.equals(value, bindings);
    }

}
