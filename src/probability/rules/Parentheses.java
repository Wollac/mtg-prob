package probability.rules;

import java.util.Stack;

/**
 * Parentheses are a special {@linkplain Operator} and has the highest possible
 * priority.
 * As Parentheses cannot be evaluated on their own, a singleton is sufficient.
 */
interface Parentheses extends Operator {

    char OPEN_PARENTHESIS_CHAR = '(';
    char CLOSE_PARENTHESIS_CHAR = ')';

    /**
     * Just a dummy value, as it will not be used explicitly.
     */
    int PARENTHESIS_PRECEDENCE = -1;

    /**
     * An open Parenthesis.
     */
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

    /**
     * A Closed Parenthesis.
     */
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
