package probability.rules.engine;

import probability.attr.ImmutableAttributeHolder;

/**
 * Operator for the logical AND, performing short-circuit evaluation.
 */
class And extends BinaryOperator {

  public And() {
    super("AND", 11);
  }

  @Override public boolean interpret(ImmutableAttributeHolder bindings) {

    return _leftOperand.interpret(bindings) && _rightOperand.interpret(bindings);
  }

}
