package probability.rules;

import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;

abstract class BinaryOperator implements Operator, Expression {

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
  public Expression parse(Stack<Token> stack) throws AttributeParseException {

    _rightOperand = extractOperand(stack);
    _leftOperand = extractOperand(stack);

    return this;
  }

  private Expression extractOperand(Stack<Token> stack) throws AttributeParseException {

    if (stack.isEmpty()) {
      throw new IllegalArgumentException("Operand missing for " + getSymbol());
    }

    Token top = stack.pop();
    return top.parse(stack);
  }

  @Override
  public TokenType getExpressionType() {
    return TokenType.OPERATOR;
  }

  @Override
  public String toString() {

    return "(" + _leftOperand + " " + getSymbol() + " " + _rightOperand + ")";
  }

}
