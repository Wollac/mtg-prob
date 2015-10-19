package probability.rules;

import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;

abstract class BinaryOperator implements Operator {

  private final String _symbol;

  private final int _precedence;

  protected Expression _leftOperand = null;

  protected Expression _rightOperand = null;

  protected BinaryOperator(String symbol, int precedence) {

    _symbol = symbol;
    _precedence = precedence;
  }

  @Override
  public abstract Operator getInstance();

  @Override
  public int getPrecedence() {

    return _precedence;
  }

  @Override
  public String getSymbol() {

    return this._symbol;
  }

  /** Parse expressions in RPN. */
  @Override
  public void parse(Stack<Expression> stack) throws AttributeParseException {

    _rightOperand = extractOperand(stack);
    _leftOperand = extractOperand(stack);
  }

  private Expression extractOperand(Stack<Expression> stack) throws AttributeParseException {

    if (stack.isEmpty()) {
      throw new IllegalArgumentException("Operand missing for " + getSymbol());
    }
    Expression result = stack.pop();
    result.parse(stack);

    return result;
  }

  @Override
  public ExpressionType getExpressionType() {
    return ExpressionType.OPERATOR;
  }

  @Override
  public String toString() {

    return "(" + _leftOperand + " " + getSymbol() + " " + _rightOperand + ")";
  }

}
