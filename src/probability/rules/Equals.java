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
	public int parse(String[] tokens, int pos, Map<String, AttributeKey<?>> variableMap, Stack<Expression> stack)
			throws AttributeParseException {

		String varName = tokens[pos - 1];
		AttributeKey<?> key = variableMap.get(varName);

		Variable<?> var = Variable.createVariable(key);

		this._leftOperand = var;
		this._rightOperand = var.createParsedVariableValue(tokens[pos + 1]);

		stack.push(this);

		return pos + 1;

	}

	@Override
	public boolean interpret(ImmutableAttributeHolder bindings) {

		AttributeKey<?> key = ((Variable<?>) this._leftOperand).getAttributeKey();
		Object lhs = bindings.getAttributeValue(key);

		Object rhs = ((VariableValue<?>) this._rightOperand).getValue();

		return Objects.equals(lhs, rhs);
	}

}