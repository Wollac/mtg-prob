package probability.rules;

import java.util.List;
import java.util.Stack;

import probability.rules.Parentheses.OpenParenthesis;
import probability.rules.Token.RulesTokenException;
import probability.rules.Token.TokenType;

class ShuntingYardAlgorithm {


    private ShuntingYardAlgorithm() {
    }

    public static Stack<Token> infix2rpn(List<Token> infixTokens) throws RulesTokenException {

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
            throw new IllegalStateException(token.getClass() + " returns the type " + TokenType.OPERATOR
                    + " but it is no sub class of " + Operator.class);
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
