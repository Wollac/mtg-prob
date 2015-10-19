package probability.core;

import java.io.File;
import java.io.IOException;

import probability.attr.IntegerAttributeKey;
import probability.rules.Rule;
import probability.rules.RuleLoader;
import probability.rules.RuleLoader.RulesParseException;
import probability.rules.VariableHolder;

public class MulliganRule {

  private static final String DEFAULT_RULES =
      "CARDS > 5 AND (LANDS < 2 OR NONLANDS < 2)\n" + "CARDS = 5 AND (LANDS < 1 OR NONLANDS < 1)\n";

  private Rule _rule;

  private final VariableHolder _variables = new VariableHolder();

  public MulliganRule() {
    registerVariables();

    _rule = getDefaultRule();
  }

  public MulliganRule(File file) {
    registerVariables();

    _rule = loadFromFile(file);
  }

  private void registerVariables() {
    _variables.registerVariable(VARIABLES.LANDS);
    _variables.registerVariable(VARIABLES.NONLANDS);
    _variables.registerVariable(VARIABLES.CARDS);
  }

  private Rule loadFromFile(File file) {

    try {
      RuleLoader loader = new RuleLoader(_variables);
      return loader.readFromFile(file);
    } catch (IOException e) {
      System.err.println("Could not read mulligan rules file: " + e.getMessage());
    } catch (RulesParseException e) {
      System.err.println("Error parsing rules file " + file.getName() + " in line "
          + e.getErrorLine() + ": " + e.getMessage());
    }
    System.err.println("Using default mulligan rules");

    return getDefaultRule();
  }

  private Rule getDefaultRule() {

    Rule rule;
    try {
      RuleLoader loader = new RuleLoader(_variables);
      rule = loader.readFromString(DEFAULT_RULES);
    } catch (IOException | RulesParseException e) {
      throw new IllegalStateException("Error parsing default rules", e);
    }

    return rule;
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
