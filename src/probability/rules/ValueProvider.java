package probability.rules;

import probability.attr.ImmutableAttributeHolder;

public interface ValueProvider<T> {

    public T getValue(ImmutableAttributeHolder bindings);

}
