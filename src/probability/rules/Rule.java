package probability.rules;

import java.util.ArrayList;
import java.util.List;

import probability.attr.AttributeHolder;

public class Rule {
	private List<Expression> expressions;

	public static class Builder {


		private List<Expression> expressions = new ArrayList<>();

		public Builder withExpression(Expression expr) {
			expressions.add(expr);
			return this;
		}

		public Rule build() {
			return new Rule(expressions);
		}
	}

	private Rule(List<Expression> expressions) {
		this.expressions = expressions;
	}

	public boolean eval(AttributeHolder bindings) {
		boolean eval = false;
		for (Expression expression : expressions) {
			eval |= expression.interpret(bindings);
		}
		
		return eval;
	}
}
