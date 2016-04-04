package probability.rules;

import java.util.Stack;

import static probability.rules.NamingConventions.*;

/**
 * Common functionality for all operators that have exactly one variable and one value as operands.
 */
abstract class VariableValueOperator extends AbstractOperator implements Operator, Expression, Token {

    Variable<?> _variable;

    Object _value;

    /**
     * Creates an operator.
     *
     * @param symbol     nonempty string used to identify the operator
     * @param precedence precedence value of the operator
     */
    VariableValueOperator(String symbol, int precedence) {

        super(symbol, precedence);
    }

    @Override
    public Expression parse(Stack<Token> stack) throws RulesTokenException {

        if (stack.size() < 2) {
            throw new RulesTokenException("Operand missing for " + getSymbol());
        }

        Token right = stack.pop();
        Token left = stack.pop();

        if (left instanceof Variable<?>) {
            parse((Variable<?>) left, right);
        } else {
            throw new RulesTokenException("The LHS of " + getSymbol() +
                    " must be a variable");
        }

        return this;
    }

    protected void parse(Variable<?> variable, Token other) throws RulesTokenException {

        _variable = variable;

        if (other instanceof Value.StringValue) {
            _value = _variable.parseValue((Value.StringValue) other);
        } else {
            throw new RulesTokenException("The RHS of " + getSymbol() +
                    " must be a value");
        }
    }

    @Override
    public String getProductionRule() {

        return EXPRESSION + ARROW_OPERATOR + VARIABLE + " " + getSymbol() + " " + STRING;
    }

    @Override
    public String toString() {

        return "(" + _variable + " " + getSymbol() + " " + _value + ")";
    }

}
