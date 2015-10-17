package probability.rules;

import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;

abstract class Operation implements Expression {

  protected String _symbol;

  protected Expression _leftOperand = null;

  protected Expression _rightOperand = null;

  protected Operation(String symbol) {

    _symbol = symbol;
  }

  public abstract Operation createInstance();

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
  public String toString() {

    StringBuilder sb = new StringBuilder();

    if (_leftOperand instanceof Operation) {
      sb.append("(" + _leftOperand + ")");
    } else {
      sb.append(_leftOperand);
    }
    sb.append(" " + getSymbol() + " ");
    if (_rightOperand instanceof Operation) {
      sb.append("(" + _rightOperand + ")");
    } else {
      sb.append(_rightOperand);
    }

    return sb.toString();
  }

}
