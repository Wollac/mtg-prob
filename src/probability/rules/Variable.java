package probability.rules;

import probability.attr.AttributeKey;
import probability.attr.AttributeKey.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;

public class Variable<T> implements Expression {

	private AttributeKey<T> _key;

	private Variable(AttributeKey<T> key) {
		_key = key;
	}

	public AttributeKey<T> getAttributeKey() {
		return _key;
	}

	public VariableValue<T> createParsedVariableValue(String valueString) throws AttributeParseException {
		return VariableValue.createVariableValue(_key, valueString);
	}

	public static <T> Variable<T> createVariable(AttributeKey<T> key) {
		return new Variable<>(key);
	}

	@Override
	public boolean interpret(ImmutableAttributeHolder bindings) {
		return true;
	}
}
