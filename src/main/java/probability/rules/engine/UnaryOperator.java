package probability.rules.engine;

import java.util.Stack;

import static probability.rules.NamingConventions.ARROW_OPERATOR;
import static probability.rules.NamingConventions.EXPRESSION;

/**
 * Common functionality for all unary operators.
 */
abstract class UnaryOperator extends AbstractOperator implements Operator, Expression, Token {

    private static final int UNARY_PRECEDENCE = 2;

    Expression _operand;

    /**
     * Creates an unary operator with the precedence of 2.
     *
     * @param symbol nonempty string used to identify the operator
     */
    UnaryOperator(String symbol) {
        super(symbol, UNARY_PRECEDENCE);
    }

    @Override
    public Expression parse(Stack<Token> stack) throws RulesTokenException {

        if (stack.isEmpty()) {
            throw new RulesTokenException("Operand missing for " + getSymbol());
        }

        Token top = stack.pop();
        _operand = top.parse(stack);

        return this;
    }

    @Override
    public String getProductionRule() {

        return EXPRESSION + ARROW_OPERATOR + getSymbol() + " " + EXPRESSION;
    }

    @Override
    public String toString() {

        return "(" + getSymbol() + " " + _operand + ")";
    }

}
