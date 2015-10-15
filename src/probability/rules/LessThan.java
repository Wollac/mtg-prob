package probability.rules;

import probability.attr.ImmutableAttributeHolder;

class LessThan extends Comparator {

  public LessThan() {

    super("<");
  }

  @Override
  public LessThan createInstance() {

    return new LessThan();
  }

  @Override
  public boolean interpret(ImmutableAttributeHolder bindings) {

    Variable<?> var = (Variable<?>) _leftOperand;
    Value<?> value = (Value<?>) _rightOperand;

    return var.compareTo(value, bindings) < 0;
  }

}
