package probability.rules.engine;

import java.util.List;
import java.util.Stack;

import probability.rules.engine.Operator;
import probability.rules.engine.Parentheses.OpenParenthesis;
import probability.rules.engine.Token;
import probability.rules.engine.Token.RulesTokenException;
import probability.rules.engine.Token.TokenType;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Simple implementation of the shunting-yard algorithm to transform mathematical expressions
 * specified in infix notation into the in Reverse Polish notation (RPN).
 */
final class ShuntingYardAlgorithm {

    private ShuntingYardAlgorithm() {
        // do not initialize
    }

    /**
     * Converts tokens in infix notation into the Reverse Polish notation.
     *
     * @param infixTokens mathematical expression specified as token in infix notation
     * @return a stack of tokens in RPN
     * @throws RulesTokenException if an error occurred
     */
    public static Stack<Token> infix2rpn(List<? extends Token> infixTokens)
            throws RulesTokenException {

        checkNotNull(infixTokens);

        Stack<Token> output = new Stack<>();
        Stack<Operator> operatorStack = new Stack<>();

        for (Token token : infixTokens) {
            TokenType type = token.getTokenType();
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

    private static void handleCloseParenthesis(Stack<Operator> operatorStack, Stack<Token> output)
            throws RulesTokenException {

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
            throw new RulesTokenException("Mismatched parentheses");
        }
    }

    private static void handleOperator(Token token, Stack<Operator> operatorStack,
                                       Stack<Token> output) {

        if (!(token instanceof Operator)) {
            throw new IllegalStateException(token.getClass() + " returns the type " +
                    TokenType.OPERATOR + " but it is no sub class of " + Operator.class);
        }

        Operator operator = (Operator) token;
        while (!operatorStack.isEmpty()) {
            Operator other = operatorStack.peek();
            if (other == OpenParenthesis.INSTANCE) {
                break;
            }
            if (operator.getPrecedence() >= other.getPrecedence()) {
                output.push(operatorStack.pop());
            } else {
                break;
            }
        }

        operatorStack.push(operator);
    }

    private static void handleOperatorStack(Stack<Operator> operatorStack, Stack<Token> output)
            throws RulesTokenException {

        while (!operatorStack.isEmpty()) {
            Operator op = operatorStack.pop();

            if (op == OpenParenthesis.INSTANCE) {
                throw new RulesTokenException("Mismatched parentheses");
            } else {
                output.push(op);
            }
        }
    }

}
