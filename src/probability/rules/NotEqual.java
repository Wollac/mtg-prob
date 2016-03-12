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

        return _variable.equals(_value, bindings);
    }

}
