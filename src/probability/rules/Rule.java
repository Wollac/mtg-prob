package probability.rules;

import java.util.ArrayList;
import java.util.List;

import probability.attr.ImmutableAttributeHolder;

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

  public boolean eval() {

    ImmutableAttributeHolder bindings = Variables.getBindings();

    for (Expression expression : expressions) {
      boolean eval = expression.interpret(bindings);

      if (eval) {
        return true;
      }
    }

    return false;
  }
}
