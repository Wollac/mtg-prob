package probability.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import probability.attr.ImmutableAttributeHolder;

public class Rule {

  private List<Expression> _expressions;

  Rule(List<Expression> expressions) {
    this._expressions = expressions;
  }

  public boolean evaluate(VariableHolder variables) {

    ImmutableAttributeHolder bindings = variables.getBindings();

    for (Expression expression : _expressions) {

      boolean eval = expression.interpret(bindings);
      if (eval) {
        return true;
      }
    }

    return false;
  }

  public List<String> toStrings() {

    List<String> result = new ArrayList<>(_expressions.size());
    _expressions.stream().forEach(e -> result.add(e.toString()));

    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    Iterator<Expression> it = _expressions.iterator();
    while (it.hasNext()) {
      sb.append(it.next());

      if (it.hasNext()) {
        sb.append(" OR ");
      }
    }

    return sb.toString();
  }

}
