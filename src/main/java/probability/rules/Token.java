package probability.rules;

import java.util.Stack;

/**
 * Class for an atomic parse element. A token can only be parsed and has a certain type which is
 * needed in the {@linkplain ShuntingYardAlgorithm}.
 */
interface Token {

    /**
     * Returns the type of the token.
     *
     * @return one from {@link TokenType}
     */
    TokenType getTokenType();

    /**
     * Parses this token into an {@linkplain Expression}. Potential operands are on the stack.
     *
     * @param stack containing tokens in RPN order
     * @return the parsed expression
     * @throws RulesTokenException if the token could not be passed
     */
    Expression parse(Stack<Token> stack) throws RulesTokenException;

    enum TokenType {
        VALUE, FUNCTION, OPERATOR, OPEN_PARENTHESIS, CLOSE_PARENTHESIS
    }

    /**
     * An Exception related to tokens and token parsing.
     */
    final class RulesTokenException extends Exception {

        private static final long serialVersionUID = -6313095374416249621L;

        public RulesTokenException(String message) {
            super(message);
        }
    }

}
