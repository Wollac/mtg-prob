package probability.rules;

import probability.attr.AttributeKey;
import probability.attr.AttributeKey.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;

public class VariableValue<T> implements Expression {

	public T _value;

	private VariableValue(T value, Class<T> type) {
		this._value = value;
	}

	public T getValue() {
		return this._value;
	}

	public static <T> VariableValue<T> createVariableValue(AttributeKey<T> key, String valueString)
			throws AttributeParseException {

		return new VariableValue<>(key.parseValue(valueString), key.getValueType());
	}

	@Override
	public boolean interpret(ImmutableAttributeHolder bindings) {
		return true;
	}

}
