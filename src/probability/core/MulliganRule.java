package probability.core;

import probability.attr.IntegerAttributeKey;
import probability.rules.ExpressionParser;
import probability.rules.Rule;
import probability.rules.Variables;

public class MulliganRule {

  static {
    Variables.registerVariable(VARIABLES.LANDS);
    Variables.registerVariable(VARIABLES.NONLANDS);
    Variables.registerVariable(VARIABLES.CARDS);
  }

  private static final String[] DEFAULT_RULES =
      {"CARDS 5 > LANDS 2 < NONLANDS 2 < OR AND", "CARDS 5 = LANDS 1 < NONLANDS 1 < OR AND"};

  private final Rule _rule;

  public MulliganRule() {
    Rule.Builder builder = new Rule.Builder();

    try {
      for (String rule : DEFAULT_RULES) {
        builder.withExpression(ExpressionParser.parse(rule));
      }
    }
    // TODO: only catch actual exceptions
    catch (Exception e) {
      throw new IllegalStateException("Error parsing default rules", e);
    }

    _rule = builder.build();
  }

  public boolean takeMulligan(int lands, int cards) {
    Variables.assignValue(VARIABLES.LANDS, lands);
    Variables.assignValue(VARIABLES.NONLANDS, cards - lands);
    Variables.assignValue(VARIABLES.CARDS, cards);

    return _rule.eval();
  }

  private interface VARIABLES {

    static final IntegerAttributeKey LANDS = new IntegerAttributeKey("LANDS");

    static final IntegerAttributeKey NONLANDS = new IntegerAttributeKey("NONLANDS");

    static final IntegerAttributeKey CARDS = new IntegerAttributeKey("CARDS");

  }

}
