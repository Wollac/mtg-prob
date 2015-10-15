package probability.rules;

import probability.attr.ImmutableAttributeHolder;

class Or extends Operation {

	public Or() {
		super("OR");
	}

	@Override
  public Or createInstance() {
		return new Or();
	}

	@Override
	public boolean interpret(ImmutableAttributeHolder bindings) {

		return _leftOperand.interpret(bindings) || _rightOperand.interpret(bindings);
	}

}
