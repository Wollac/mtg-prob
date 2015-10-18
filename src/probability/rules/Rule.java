package probability.rules;

import java.util.ArrayList;
import java.util.Iterator;
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

  public List<String> toStrings() {

    List<String> result = new ArrayList<>(expressions.size());
    expressions.stream().forEach(e -> result.add(e.toString()));

    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    Iterator<Expression> it = expressions.iterator();
    while (it.hasNext()) {
      sb.append(it.next());

      if (it.hasNext()) {
        sb.append(" OR ");
      }
    }

    return sb.toString();
  }
}
