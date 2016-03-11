package probability.rules;

import java.util.Stack;

import probability.rules.Value.StringValue;

abstract class Comparator extends BinaryOperator {

  protected Comparator(String symbol, int precedence) {
    super(symbol, precedence);
  }

  @Override
  public Expression parse(Stack<Token> stack) throws RulesTokenException {

    Token right = stack.pop();
    Token left = stack.pop();

    if (!(left instanceof Variable<?>)) {
      throw new RulesTokenException("The LHS of operator " + getSymbol() + " must be a variable");
    }
    Variable<?> var = (Variable<?>) left;
    _leftOperand = var;

    if (!Comparable.class.isAssignableFrom(var.getType())) {
      throw new RulesTokenException("Cannot compare a variable of type " + var.getTypeName());
    }

    if (right instanceof StringValue) {
      _rightOperand = var.createParsedValue((StringValue) right);
    } else {
      throw new RulesTokenException("The RHS of operator " + getSymbol() + " must be a value");
    }

    return this;
  }

}
