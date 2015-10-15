package probability.rules;

import java.util.Map;
import java.util.Objects;
import java.util.Stack;

import probability.attr.AttributeKey;
import probability.attr.AttributeKey.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;

public class Equals extends Operation {

    public Equals() {

        super("=");
    }

    @Override
    public Equals copy() {

        return new Equals();
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

        if(right instanceof StringValue) {
            _rightOperand = var.createParsedVariableValue(right);
        }
        else {
            throw new IllegalArgumentException("The RHS of " + getSymbol()
                + " must be a variable or a value");
        }
    }

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {
        
        ValueProvider<?> lhs = (ValueProvider<?>) _leftOperand;
        ValueProvider<?> rhs = (ValueProvider<?>) _rightOperand;

        return Objects.equals(lhs.getValue(bindings), rhs.getValue(bindings));
    }

}