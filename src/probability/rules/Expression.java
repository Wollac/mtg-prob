package probability.rules;

import probability.attr.ImmutableAttributeHolder;

interface Expression {

  boolean interpret(ImmutableAttributeHolder bindings);

}
