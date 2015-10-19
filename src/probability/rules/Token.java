package probability.rules;

import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;

interface Token {

  public enum TokenType {
    VALUE, FUNCTION, OPERATOR, OPEN_PARENTHESIS, CLOSE_PARENTHESIS;
  }

  public TokenType getExpressionType();

  public Expression parse(Stack<Token> stack) throws AttributeParseException;
}
