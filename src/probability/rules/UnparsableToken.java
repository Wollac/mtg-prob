package probability.rules;

import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;

abstract class UnparsableToken implements Token {

  @Override
  public Expression parse(Stack<Token> stack) throws AttributeParseException {
    throw new IllegalStateException(
        getClass().getName() + " cannot be parsed into an expression");
  }

}
