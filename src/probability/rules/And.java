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

	@Override
	public int parse(String[] tokens, int pos, Map<String, AttributeKey<?>> variableMap, Stack<Expression> stack)
			throws AttributeParseException {
		Expression left = stack.pop();
		int i = findNextExpression(tokens, pos + 1, variableMap, stack);
		Expression right = stack.pop();

		this._leftOperand = left;
		this._rightOperand = right;

		stack.push(this);

		return i;
	}

}
