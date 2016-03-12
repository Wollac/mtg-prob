package probability.rules;

import java.util.Stack;

abstract class Comparator extends VariableValueOperator {

    Comparator(String symbol) {
        super(symbol, 6);
    }

    @Override
    public Expression parse(Stack<Token> stack) throws RulesTokenException {

        super.parse(stack);


        if (!Comparable.class.isAssignableFrom(_variable.getType())) {

            throw new RulesTokenException("Cannot compare a variable of type " +
                    _variable.getTypeName());
        }

        return this;
    }

}
