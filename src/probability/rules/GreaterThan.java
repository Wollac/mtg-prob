package probability.rules;

import probability.attr.ImmutableAttributeHolder;

class GreaterThan extends Comparator {

  public GreaterThan() {

    super(">");
  }

  @Override
  public GreaterThan createInstance() {

    return new GreaterThan();
  }

  @Override
  public boolean interpret(ImmutableAttributeHolder bindings) {

    Variable<?> var = (Variable<?>) _leftOperand;
    Value<?> value = (Value<?>) _rightOperand;

    return var.compareTo(value, bindings) > 0;
  }

}
