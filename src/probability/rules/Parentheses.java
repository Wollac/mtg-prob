package probability.rules;

import java.util.Stack;

interface Parentheses extends Operator, Token {

  char OPEN_PARENTHESIS_CHAR = '(';
  char CLOSE_PARENTHESIS_CHAR = ')';

  int PARENTHESIS_PRECEDENCE = -1;

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
    public TokenType getTokenType() {
      return TokenType.OPEN_PARENTHESIS;
    }

    @Override
    public Expression parse(Stack<Token> stack) throws RulesTokenException {
      throw new RulesTokenException(getClass().getName() + " cannot be parsed into an expression");
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
    public TokenType getTokenType() {
      return TokenType.CLOSE_PARENTHESIS;
    }

    @Override
    public Expression parse(Stack<Token> stack) throws RulesTokenException {
      throw new RulesTokenException(getClass().getName() + " cannot be parsed into an expression");
    }

  }
}
