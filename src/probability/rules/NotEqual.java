package probability.rules;

import probability.attr.ImmutableAttributeHolder;

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

        Variable<?> var = (Variable<?>) _leftOperand;
        Value<?> value = (Value<?>) _rightOperand;

        return !var.equals(value, bindings);
    }

}
