package probability.rules;

import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;
import probability.rules.Value.StringValue;

class Equals extends Operation {

  public Equals() {

    super("=");
  }

  @Override
  public Equals createInstance() {

    return new Equals();
  }

  @Override
  public void parse(Stack<Expression> stack) throws AttributeParseException {

    Expression right = stack.pop();
    Expression left = stack.pop();

    if (!(left instanceof Variable<?>)) {
      throw new IllegalArgumentException("The LHS of " + getSymbol() + " must be a variable");
    }
    _leftOperand = left;
    Variable<?> var = (Variable<?>) left;

    if (right instanceof StringValue) {
      _rightOperand = var.createParsedValue((StringValue) right);
    } else {
      throw new IllegalArgumentException("The RHS of " + getSymbol() + " must be a  value");
    }
  }

  @Override
  public boolean interpret(ImmutableAttributeHolder bindings) {

    Variable<?> var = (Variable<?>) _leftOperand;
    Value<?> value = (Value<?>) _rightOperand;

    return var.equals(value, bindings);
  }

}
