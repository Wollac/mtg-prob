package probability.rules;

import probability.attr.ImmutableAttributeHolder;
import probability.attr.AttributeKey.AttributeParseException;

public class LessThan {

    public LessThan() {

        super("<");
    }

    @Override
    public LessThan copy() {

        return new LessThan();
    }

    @Override
    public void parse(Stack<Expression> stack) throws AttributeParseException {

        Expression right = stack.pop();
        Expression left = stack.pop();

        if(!(left instanceof Variable<?>)) {
            throw new IllegalArgumentException("The LHS of " + getSymbol()
                + " must be a variable");
        }
        _leftOperand = left;
        Variable<?> var = (Variable<?>) left;

        if(!Comparable.class.isAssignableFrom(var.getType())) {
            throw new IllegalArgumentException("Cannot compare a variable of type "
                + var.getType());
        }

        if(right instanceof StringValue) {
            _rightOperand = var.createParsedVariableValue(right);
        }
        else {
            throw new IllegalArgumentException("The RHS of " + getSymbol()
                + " must be a value");
        }
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {

        ValueProvider<?> lhs = (ValueProvider<?>) _leftOperand;
        ValueProvider<?> rhs = (ValueProvider<?>) _rightOperand;

        return ((Comparable) lhs.getValue(bindings)).compareTo(rhs.getValue(bindings)) < 0;
    }
}
