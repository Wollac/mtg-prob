package probability.rules;

import probability.attr.ImmutableAttributeHolder;

interface ValueProvider<T> {

    public T getValue(ImmutableAttributeHolder bindings);

}
