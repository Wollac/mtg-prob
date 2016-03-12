package probability.rules;

import probability.attr.ImmutableAttributeHolder;

class LessThan extends Comparator {

    public LessThan() {

        super("<");
    }

    @Override
    public LessThan getInstance() {

        return new LessThan();
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {

        return _variable.compareTo(_value, bindings) < 0;
    }

}
