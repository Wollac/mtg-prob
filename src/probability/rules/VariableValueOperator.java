package probability.rules;

import java.util.Stack;

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
        } else if (right instanceof Variable<?>) {
            parse((Variable<?>) right, left);
        } else {
            throw new RulesTokenException("One operand of " + getSymbol() +
                    " must be a variable");
        }

        return this;
    }

    private void parse(Variable<?> variable, Token other) throws RulesTokenException {

        _variable = variable;

        if (other instanceof Value.StringValue) {
            _value = _variable.parseValue((Value.StringValue) other);
        } else {
            throw new RulesTokenException("One operand of " + getSymbol() +
                    " must be a value");
        }
    }


    @Override
    public String toString() {

        return "(" + _variable + " " + getSymbol() + " " + _value + ")";
    }

}
