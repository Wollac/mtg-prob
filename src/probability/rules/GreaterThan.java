package probability.rules;

import probability.attr.ImmutableAttributeHolder;

class GreaterThan extends Comparator {

    public GreaterThan() {

        super(">");
    }

    @Override
    public GreaterThan getInstance() {

        return new GreaterThan();
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {

        return _variable.compareTo(_value, bindings) > 0;
    }

}
