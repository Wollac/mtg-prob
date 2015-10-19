package probability.rules;

import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;
import probability.rules.Value.StringValue;

abstract class Comparator extends BinaryOperator {

  protected Comparator(String symbol, int precedence) {
    super(symbol, precedence);
  }

  @Override
  public Expression parse(Stack<Token> stack) throws AttributeParseException {

    Token right = stack.pop();
    Token left = stack.pop();

    if (!(left instanceof Variable<?>)) {
      throw new IllegalArgumentException("The LHS of " + getSymbol() + " must be a variable");
    }
    Variable<?> var = (Variable<?>) left;
    _leftOperand = var;

    if (!Comparable.class.isAssignableFrom(var.getType())) {
      new IllegalArgumentException("Cannot compare a variable of type " + var.getTypeName());
    }

    if (right instanceof StringValue) {
      _rightOperand = var.createParsedValue((StringValue) right);
    } else {
      throw new IllegalArgumentException("The RHS of operator " + getSymbol() + " must be a value");
    }

    return this;
  }

}
