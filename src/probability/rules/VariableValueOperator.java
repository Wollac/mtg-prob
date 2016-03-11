package probability.rules;

import java.util.Stack;


abstract class VariableValueOperator extends BinaryOperator {

    VariableValueOperator(String symbol, int precedence) {
        super(symbol, precedence);
    }

    @Override
    public Expression parse(Stack<Token> stack) throws RulesTokenException {

        Token right = stack.pop();
        Token left = stack.pop();

        if (!(left instanceof Variable<?>)) {
            throw new RulesTokenException("The LHS of " + getSymbol() + " must be a variable");
        }
        Variable<?> var = (Variable<?>) left;
        _leftOperand = var;

        if (right instanceof Value.StringValue) {
            _rightOperand = var.createParsedValue((Value.StringValue) right);
        } else {
            throw new RulesTokenException("The RHS of " + getSymbol() + " must be a  value");
        }

        return this;
    }

}
