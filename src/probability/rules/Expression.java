package probability.rules;

import probability.attr.ImmutableAttributeHolder;

public interface Expression {

	public boolean interpret(ImmutableAttributeHolder bindings);

}
