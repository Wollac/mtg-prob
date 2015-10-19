package probability.rules;

import java.util.List;
import java.util.Stack;

import probability.rules.Parentheses.OpenParenthesis;
import probability.rules.Token.TokenType;

class ShuntingYardAlgorithm {

  public static Stack<Token> infix2rpn(List<Token> infixExpressions) {

    Stack<Token> output = new Stack<>();
    Stack<Operator> operatorStack = new Stack<>();

    for (Token token : infixExpressions) {
      TokenType type = token.getExpressionType();
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

  private static void handleCloseParenthesis(Stack<Operator> operatorStack, Stack<Token> output) {

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

  private static void handleOperator(Token token, Stack<Operator> operatorStack,
      Stack<Token> output) {

    if (!(token instanceof Operator)) {
      throw new IllegalStateException(token.getClass() + " returns the type " + TokenType.OPERATOR
          + " but it is no extention of " + Operator.class);
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

  private static void handleOperatorStack(Stack<Operator> operatorStack, Stack<Token> output) {
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
