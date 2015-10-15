package probability.rules;

import java.util.Map;
import java.util.Stack;

import probability.attr.AttributeKey;
import probability.attr.AttributeKey.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;

public class Or extends Operation {

	public Or() {
		super("OR");
	}

	public Or copy() {
		return new Or();
	}

	@Override
	public boolean interpret(ImmutableAttributeHolder bindings) {

		return _leftOperand.interpret(bindings) || _rightOperand.interpret(bindings);
	}

}
