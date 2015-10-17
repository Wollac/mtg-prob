package probability.rules;

import probability.attr.ImmutableAttributeHolder;

class And extends Operation {

  public And() {
    super("AND");
  }

  @Override
  public And createInstance() {
    return new And();
  }

  @Override
  public boolean interpret(ImmutableAttributeHolder bindings) {

    return _leftOperand.interpret(bindings) && _rightOperand.interpret(bindings);
  }

}
