package probability.rules;

import probability.attr.ImmutableAttributeHolder;

interface Expression {

  public boolean interpret(ImmutableAttributeHolder bindings);
}
