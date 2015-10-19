package probability.core;

import probability.attr.IntegerAttributeKey;
import probability.rules.ExpressionParser;
import probability.rules.Rule;
import probability.rules.VariableHolder;

public class MulliganRule {

  private static final String[] DEFAULT_RULES_RPN = {
      "(CARDS > 5) AND ((LANDS < 2) OR (NONLANDS < 2))",
      "(CARDS = 5) AND ((LANDS < 1) OR (NONLANDS < 1))"};

  private final Rule _rule;

  private final VariableHolder _variables = new VariableHolder();

  public MulliganRule() {

    registerVariables();
    _rule = getDefaultRule(_variables);
  }

  private void registerVariables() {
    _variables.registerVariable(VARIABLES.LANDS);
    _variables.registerVariable(VARIABLES.NONLANDS);
    _variables.registerVariable(VARIABLES.CARDS);
  }

  private static Rule getDefaultRule(VariableHolder variables) {
    Rule.Builder builder = new Rule.Builder();

    try {
      for (String rule : DEFAULT_RULES_RPN) {
        builder.withExpressions(ExpressionParser.parseString(variables, rule));
      }
    }
    // TODO: only catch actual exceptions
    catch (Exception e) {
      throw new IllegalStateException("Error parsing default rules", e);
    }

    return builder.build();
  }

  public boolean takeMulligan(int lands, int cards) {
    _variables.assignValue(VARIABLES.LANDS, lands);
    _variables.assignValue(VARIABLES.NONLANDS, cards - lands);
    _variables.assignValue(VARIABLES.CARDS, cards);

    return _rule.evaluate(_variables);
  }

  public String toFormatedString() {

    StringBuilder sb = new StringBuilder();

    for (String expression : _rule.toStrings()) {
      if (expression.startsWith("(") && expression.endsWith(")")) {
        sb.append(expression.substring(1, expression.length() - 1));
      } else {
        sb.append(expression);
      }
      sb.append('\n');
    }

    return sb.toString() + "\n";
  }

  private interface VARIABLES {

    static final IntegerAttributeKey LANDS = new IntegerAttributeKey("LANDS");

    static final IntegerAttributeKey NONLANDS = new IntegerAttributeKey("NONLANDS");

    static final IntegerAttributeKey CARDS = new IntegerAttributeKey("CARDS");
  }

}
