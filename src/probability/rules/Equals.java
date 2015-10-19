package probability.rules;

import java.util.Stack;

import probability.attr.ImmutableAttributeHolder;
import probability.rules.Value.StringValue;

class Equals extends BinaryOperator {

  public Equals() {

    super("=", 7);
  }

  @Override
  public Equals getInstance() {

    return new Equals();
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

    if (right instanceof StringValue) {
      _rightOperand = var.createParsedValue((StringValue) right);
    } else {
      throw new RulesTokenException("The RHS of " + getSymbol() + " must be a  value");
    }

    return this;
  }

  @Override
  public boolean interpret(ImmutableAttributeHolder bindings) {

    Variable<?> var = (Variable<?>) _leftOperand;
    Value<?> value = (Value<?>) _rightOperand;

    return var.equals(value, bindings);
  }

}
