package probability.rules;

import java.util.Stack;

interface Token {

  public enum TokenType {
    VALUE, FUNCTION, OPERATOR, OPEN_PARENTHESIS, CLOSE_PARENTHESIS;
  }

  /**
   * Returns the type of the token.
   * 
   * @return one from {@link TokenType}
   */
  public TokenType getTokenType();

  /**
   * Parses this token into an expression. Potential operands are on the stack.
   * 
   * @param stack containing tokens in RPN order
   * @return the parsed expression
   * @throws RulesTokenException if the token could not be passed
   */
  public Expression parse(Stack<Token> stack) throws RulesTokenException;

  /**
   * An Exception related to tokens and token parsing.
   */
  public final static class RulesTokenException extends Exception {

    private static final long serialVersionUID = -6313095374416249621L;

    public RulesTokenException(String message) {
      super(message);
    }
  }

}
