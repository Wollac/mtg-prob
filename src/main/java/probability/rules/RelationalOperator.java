package probability.rules;

import java.util.Stack;

/**
 * Common functionality of all relational operators. It is only possible to compare variables with
 * values and the variable type must implement the {@linkplain Comparable} interface.
 */
abstract class RelationalOperator extends VariableValueOperator {

    private static final int RELATIONAL_PRECEDENCE = 6;

    /**
     * Creates an relational operator with the precedence of 6.
     *
     * @param symbol nonempty string used to identify the operator
     */
    RelationalOperator(String symbol) {
        super(symbol, RELATIONAL_PRECEDENCE);
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
