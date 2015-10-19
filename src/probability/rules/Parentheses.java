package probability.rules;

import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;

interface Parentheses extends Operator, Token {

  static final char OPEN_PARENTHESIS_CHAR = '(';
  static final char CLOSE_PARENTHESIS_CHAR = ')';

  static final int PARENTHESIS_PRECEDENCE = -1;

  enum OpenParenthesis implements Parentheses {
    INSTANCE;

    @Override
    public Operator getInstance() {
      return INSTANCE;
    }

    @Override
    public String getSymbol() {
      return String.valueOf(OPEN_PARENTHESIS_CHAR);
    }

    @Override
    public int getPrecedence() {
      return PARENTHESIS_PRECEDENCE;
    }

    @Override
    public TokenType getExpressionType() {
      return TokenType.OPEN_PARENTHESIS;
    }

    @Override
    public Expression parse(Stack<Token> stack) throws AttributeParseException {
      throw new IllegalStateException(
          getClass().getName() + " cannot be parsed into an expression");
    }

  }

  enum CloseParenthesis implements Parentheses {
    INSTANCE;

    @Override
    public Operator getInstance() {
      return INSTANCE;
    }

    @Override
    public String getSymbol() {
      return String.valueOf(CLOSE_PARENTHESIS_CHAR);
    }

    @Override
    public int getPrecedence() {
      return PARENTHESIS_PRECEDENCE;
    }

    @Override
    public TokenType getExpressionType() {
      return TokenType.CLOSE_PARENTHESIS;
    }

    @Override
    public Expression parse(Stack<Token> stack) throws AttributeParseException {
      throw new IllegalStateException(
          getClass().getName() + " cannot be parsed into an expression");
    }

  }
}
