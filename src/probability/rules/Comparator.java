package probability.rules;

import java.util.Stack;

abstract class Comparator extends VariableValueOperator {

    Comparator(String symbol, int precedence) {
        super(symbol, precedence);
    }

    @Override
    public Expression parse(Stack<Token> stack) throws RulesTokenException {

        super.parse(stack);

        Variable<?> var = (Variable<?>) _leftOperand;

        if (!Comparable.class.isAssignableFrom(var.getType())) {
            throw new RulesTokenException("Cannot compare a variable of type " + var.getTypeName());
        }

        return this;
    }

}
