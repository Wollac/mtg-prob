package probability.rules;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;
import probability.rules.Parentheses.CloseParenthesis;
import probability.rules.Parentheses.OpenParenthesis;
import probability.rules.StringTokenizer.TokenType;
import probability.rules.Value.StringValue;

public class ExpressionParser {

  public static List<Expression> parseString(VariableHolder variables, String expression)
      throws AttributeParseException, ParseException {

    ArrayList<Expression> parsedExpressions = new ArrayList<Expression>();

    List<Expression> expressionList = new ArrayList<>();
    try {
      StringTokenizer tokenizer = new StringTokenizer(expression);
      while (tokenizer.nextToken() != TokenType.EOF) {
        switch (tokenizer.tokenType()) {
          case EOL:
            parseInfixExpression(expressionList, parsedExpressions);
            break;
          case STRING:
            expressionList.add(token2Expression(variables, tokenizer.tokenValue()));
            break;
          case QUOTED_STRING:
            expressionList.add(new StringValue(tokenizer.tokenValue()));
            break;
          case OPEN_PARENTHESIS:
            expressionList.add(OpenParenthesis.INSTANCE);
            break;
          case CLOSE_PARENTHESIS:
            expressionList.add(CloseParenthesis.INSTANCE);
            break;
          case EOF:
          case INVALID:
            throw new ParseException(
                "Parsing Error: unrecognized token: " + tokenizer.tokenValue(),
                tokenizer.currentLine());
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException("Error while tokenizing string", e);
    }

    parseInfixExpression(expressionList, parsedExpressions);

    return parsedExpressions;
  }

  private static void parseInfixExpression(List<Expression> infixExpressions,
      List<Expression> parsedExpressions) throws AttributeParseException {

    Stack<Expression> rpnStack = ShuntingYardAlgorithm.infix2rpn(infixExpressions);
    parseRPNStack(rpnStack, parsedExpressions);
  }

  private static void parseRPNStack(Stack<Expression> stack, List<Expression> parsedExpressions)
      throws AttributeParseException {

    if (stack.isEmpty()) {
      return;
    }

    Expression top = stack.pop();
    top.parse(stack);

    if (!stack.isEmpty()) {
      throw new IllegalArgumentException();
    }

    parsedExpressions.add(top);
  }

  private static Expression token2Expression(VariableHolder variables, String token) {

    Operations op = Operations.getOperation(token);
    if (op != null) {
      return op.getInstance();
    }

    if (variables.isRegistered(token)) {
      return variables.getVariable(token);
    }

    return new StringValue(token);
  }

}
