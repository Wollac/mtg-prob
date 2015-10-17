package probability.core;

import probability.attr.AttributeKey.AttributeParseException;
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

  private final Rule _rule;

  public MulliganRule() {
    Rule.Builder builder = new Rule.Builder();

    try {
      builder
          .withExpression(ExpressionParser.fromString("CARDS 5 > LANDS 2 < NONLANDS 2 < OR AND"));
      builder
          .withExpression(ExpressionParser.fromString("CARDS 5 = LANDS 1 < NONLANDS 1 < OR AND"));
    } catch (AttributeParseException e) {
      e.printStackTrace();
    }

    _rule = builder.build();
  }

  public boolean takeMulligan(int lands, int cards) {
    Variables.assignValue(VARIABLES.LANDS, lands);
    Variables.assignValue(VARIABLES.NONLANDS, cards - lands);
    Variables.assignValue(VARIABLES.CARDS, cards);

    return _rule.eval();
  }

  private static final class VARIABLES {

    private static final IntegerAttributeKey LANDS = new IntegerAttributeKey("LANDS");

    private static final IntegerAttributeKey NONLANDS = new IntegerAttributeKey("NONLANDS");

    private static final IntegerAttributeKey CARDS = new IntegerAttributeKey("CARDS");

  }

}
