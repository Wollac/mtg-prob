package probability.rules;

import java.util.Stack;

import static probability.rules.NamingConventions.ARROW_OPERATOR;
import static probability.rules.NamingConventions.EXPRESSION;

/**
 * Parentheses are a special {@linkplain Operator} and has the highest possible priority. As
 * Parentheses cannot be evaluated on their own, a singleton is sufficient.
 */
interface Parentheses extends Operator {

    char OPEN_PARENTHESIS_CHAR = '(';
    char CLOSE_PARENTHESIS_CHAR = ')';

    /**
     * Just a dummy value, as it will not be used explicitly.
     */
    int PARENTHESIS_PRECEDENCE = -1;

    static String getProductionRules() {
        return EXPRESSION + ARROW_OPERATOR + OPEN_PARENTHESIS_CHAR + EXPRESSION + CLOSE_PARENTHESIS_CHAR;
    }

    /**
     * An open Parenthesis.
     */
    enum OpenParenthesis implements Parentheses {
        INSTANCE;

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
        public Expression parse(Stack<Token> stack) {
            throw new IllegalStateException();
        }

        @Override
        public String getProductionRule() {
            throw new IllegalStateException();
        }

    }

    /**
     * A Closed Parenthesis.
     */
    enum CloseParenthesis implements Parentheses {
        INSTANCE;

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
        public Expression parse(Stack<Token> stack) {
            throw new IllegalStateException();
        }

        @Override
        public String getProductionRule() {
            throw new IllegalStateException();
        }
    }
}
