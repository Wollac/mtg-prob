package probability.rules;

import probability.attr.ImmutableAttributeHolder;

class And extends BinaryOperator {

  public And() {
    super("AND", 11);
  }

  @Override
  public And getInstance() {
    return new And();
  }

  @Override
  public boolean interpret(ImmutableAttributeHolder bindings) {

    return _leftOperand.interpret(bindings) && _rightOperand.interpret(bindings);
  }

}
