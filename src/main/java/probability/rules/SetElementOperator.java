package probability.rules;

import java.util.Set;
import java.util.Stack;

abstract class SetElementOperator extends VariableValueOperator {

    Variable<? extends Set<?>> _variable;

    Object _value;

    /**
     * Creates an operator.
     *
     * @param symbol     nonempty string used to identify the operator
     * @param precedence precedence value of the operator
     */
    SetElementOperator(String symbol, int precedence) {
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
                    " must be a set variable");
        }

        return this;
    }

    @Override
    protected void parse(Variable<?> variable, Token other) throws RulesTokenException {

        _variable = getSetVariable(variable);

        if (!(other instanceof Value.StringValue)) {
            throw new RulesTokenException("One operand of " + getSymbol() +
                    " must be a value");
        }

        Set<?> values = _variable.parseValue((Value.StringValue) other);
        if (values == null || values.size() != 1) {
            throw new RulesTokenException("The RHS of " + getSymbol() +
                    " must be exactly one element");
        }
        _value = values.iterator().next();
    }

    private Variable<? extends Set<?>> getSetVariable(Variable<?> variable) throws RulesTokenException {

        if (!Set.class.isAssignableFrom(variable.getType())) {

            throw new RulesTokenException(getSymbol() + " can only be applied to variables of the" +
                    " type Set");
        }

        @SuppressWarnings("unchecked")
        Variable<? extends Set<?>> result = (Variable<? extends Set<?>>) variable;

        return result;
    }

    @Override
    public String toString() {

        String valueString = _value.toString();

        if (valueString.indexOf(' ') >= 0) {
            valueString = '"' + valueString + '"';
        }

        return "(" + _variable + " " + getSymbol() + " " + valueString + ")";
    }
}
