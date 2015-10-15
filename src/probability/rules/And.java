package probability.rules;

import java.util.Map;
import java.util.Stack;

import probability.attr.AttributeKey;
import probability.attr.AttributeKey.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;

public class And extends Operation {

	public And() {
		super("AND");
	}

	public And copy() {
		return new And();
	}

	@Override
	public boolean interpret(ImmutableAttributeHolder bindings) {

		return _leftOperand.interpret(bindings) && _rightOperand.interpret(bindings);
	}

}
