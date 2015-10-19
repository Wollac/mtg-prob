package probability.rules;

import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;

interface Expression {

  public enum ExpressionType {
    VALUE, FUNCTION, OPERATOR, OPEN_PARENTHESIS, CLOSE_PARENTHESIS;
  }

  public ExpressionType getExpressionType();

  public abstract boolean interpret(ImmutableAttributeHolder bindings);

  public abstract void parse(Stack<Expression> stack) throws AttributeParseException;

}
