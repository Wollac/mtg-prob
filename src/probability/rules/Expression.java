package probability.rules;

import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;

interface Expression {

    public boolean interpret(ImmutableAttributeHolder bindings);

    public void parse(Stack<Expression> stack) throws AttributeParseException;

}
