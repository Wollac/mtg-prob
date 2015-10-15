package probability.rules;

import probability.attr.ImmutableAttributeHolder;
import probability.attr.AttributeKey.AttributeParseException;

public interface Expression {

    public boolean interpret(ImmutableAttributeHolder bindings);

    public void parse(Stack<Expression> stack) throws AttributeParseException;

}
