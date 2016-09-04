package probability.rules.engine;

import probability.attr.ImmutableAttributeHolder;

class Contains extends SetElementOperator {

    Contains() {

        super("CONTAINS", 6);
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {

        return _variable.getValue(bindings).contains(_value);
    }
}
