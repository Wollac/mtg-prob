package probability.rules;

import java.util.List;
import java.util.Stack;

import probability.rules.Expression.ExpressionType;
import probability.rules.Parentheses.OpenParenthesis;

class ShuntingYardAlgorithm {

  public static Stack<Expression> infix2rpn(List<Expression> infixExpressions) {

    Stack<Expression> output = new Stack<Expression>();
    Stack<Operator> operatorStack = new Stack<Operator>();

    for (Expression token : infixExpressions) {
      ExpressionType type = token.getExpressionType();
      switch (type) {
        case VALUE:
        case FUNCTION:
          output.push(token);
          break;
        case OPERATOR:
          handleOperator(token, operatorStack, output);
          break;
        case OPEN_PARENTHESIS:
          handleOpenParenthesis(operatorStack);
          break;
        case CLOSE_PARENTHESIS:
          handleCloseParenthesis(operatorStack, output);
          break;
      }
    }

    handleOperatorStack(operatorStack, output);

    return output;
  }

  private static void handleOpenParenthesis(Stack<Operator> operatorStack) {
    operatorStack.push(OpenParenthesis.INSTANCE);
  }

  private static void handleCloseParenthesis(Stack<Operator> operatorStack, Stack<Expression> output) {

    boolean matchingParenthesis = false;
    while (!operatorStack.isEmpty()) {
      Operator op = operatorStack.pop();

      if (op == OpenParenthesis.INSTANCE) {
        matchingParenthesis = true;
        break;
      } else {
        output.push(op);
      }
    }

    if (!matchingParenthesis) {
      throw new IllegalArgumentException("mismatched parentheses");
    }
  }

  private static void handleOperator(Expression token, Stack<Operator> operatorStack,
      Stack<Expression> output) {

    if (!(token instanceof Operator)) {
      throw new IllegalStateException(token.getClass() + " returns the type "
          + ExpressionType.OPERATOR + " but it is no extention of " + Operator.class);
    }

    Operator operator = (Operator) token;
    while (!operatorStack.isEmpty()) {
      Operator other = operatorStack.peek();
      if (operator.getPrecedence() <= other.getPrecedence()) {
        output.push(operatorStack.pop());
      } else {
        break;
      }
    }

    operatorStack.push(operator);
  }

  private static void handleOperatorStack(Stack<Operator> operatorStack, Stack<Expression> output) {
    while (!operatorStack.isEmpty()) {
      Operator op = operatorStack.pop();

      if (op == OpenParenthesis.INSTANCE) {
        // TODO: throw less general exception
        throw new IllegalArgumentException("mismatched parentheses");
      } else {
        output.push(op);
      }
    }
  }

}
