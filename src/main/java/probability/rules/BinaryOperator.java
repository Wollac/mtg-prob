package probability.rules;

import java.util.Stack;

import static probability.rules.NamingConventions.ARROW_OPERATOR;
import static probability.rules.NamingConventions.EXPRESSION;

/**
 * Common functionality for all binary operators.
 */
abstract class BinaryOperator extends AbstractOperator implements Operator, Expression, Token {

    Expression _leftOperand;

    Expression _rightOperand;

    /**
     * Creates a binary operator.
     *
     * @param symbol     nonempty string used to identify the operator
     * @param precedence precedence value of the operator
     */
    BinaryOperator(String symbol, int precedence) {

        super(symbol, precedence);
    }

    @Override
    public Expression parse(Stack<Token> stack) throws RulesTokenException {

        _rightOperand = extractOperand(stack);
        _leftOperand = extractOperand(stack);

        return this;
    }

    private Expression extractOperand(Stack<Token> stack) throws RulesTokenException {

        if (stack.isEmpty()) {
            throw new RulesTokenException("Operand missing for " + getSymbol());
        }

        Token top = stack.pop();
        return top.parse(stack);
    }

    @Override
    public String getProductionRule() {

        return EXPRESSION + ARROW_OPERATOR + EXPRESSION + " " + getSymbol() + " " + EXPRESSION;
    }

    @Override
    public String toString() {

        return "(" + _leftOperand + " " + getSymbol() + " " + _rightOperand + ")";
    }

}
