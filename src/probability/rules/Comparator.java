package probability.rules;

import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;
import probability.rules.Value.StringValue;

abstract class Comparator extends Operation {

  protected Comparator(String symbol) {
    super(symbol);
  }

  @Override
  public void parse(Stack<Expression> stack) throws AttributeParseException {

    Expression right = stack.pop();
    Expression left = stack.pop();

    if (!(left instanceof Variable<?>)) {
      throw new IllegalArgumentException("The LHS of " + getSymbol() + " must be a variable");
    }
    _leftOperand = left;
    Variable<?> var = (Variable<?>) left;

    if (!Comparable.class.isAssignableFrom(var.getType())) {
      new IllegalArgumentException("Cannot compare a variable of type " + var.getTypeName());
    }

    if (right instanceof StringValue) {
      _rightOperand = var.createParsedValue((StringValue) right);
    } else {
      throw new IllegalArgumentException("The RHS of operator " + getSymbol() + " must be a value");
    }
  }

}
