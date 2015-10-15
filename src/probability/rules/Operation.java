package probability.rules;

import java.util.Map;
import java.util.Stack;

import probability.attr.AttributeKey;
import probability.attr.AttributeKey.AttributeParseException;

public abstract class Operation implements Expression {

	protected String _symbol;

	protected Expression _leftOperand = null;
	protected Expression _rightOperand = null;

	protected Operation(String symbol) {
		this._symbol = symbol;
	}

	public abstract Operation copy();

	public String getSymbol() {
		return this._symbol;
	}

	public abstract int parse(final String[] tokens, final int pos, Map<String, AttributeKey<?>> variableMap,
			final Stack<Expression> stack) throws AttributeParseException;

	protected Integer findNextExpression(String[] tokens, int pos, Map<String, AttributeKey<?>> variableMap,
			Stack<Expression> stack) throws AttributeParseException {

		Operations operations = Operations.INSTANCE;

		for (int i = pos; i < tokens.length; i++) {
			Operation op = operations.getOperation(tokens[i]);
			if (op != null) {
				op = op.copy();
				// we found an operation
				i = op.parse(tokens, i, variableMap, stack);

				return i;
			}
		}

		return null;
	}

}