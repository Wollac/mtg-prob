package probability.core;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import probability.attr.*;
import probability.core.land.Land;
import probability.rules.Rule;
import probability.rules.RuleLoader;
import probability.rules.RuleLoader.RulesParseException;
import probability.rules.VariableHolder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

public class MulliganRule {

    private static final String DEFAULT_RULES_RESOURCE_NAME = "default_mulligan_rule.txt";

    private static final ResourceBundle bundle = ResourceBundle.getBundle("variables");

    private final Rule _rule;

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

        List<AttributeKey<?>> variableKeys = AttributeUtils.getAttributeKeys(VARIABLES.class);

        _variables.registerVariables(variableKeys);
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

    public void print() {

        for (AttributeKey<?> var : AttributeUtils.getAttributeKeys(VARIABLES.class)) {
            System.out.println(var.getValueType().getSimpleName() + '\t' + var.getName() + ": " + bundle.getString(var.getName()));
        }

    }

    private Rule getDefaultRule() {

        Rule rule;
        try {
            RuleLoader loader = new RuleLoader(_variables);
            rule = loader.readFromString(getDefaultRulesStringFromResources());
        } catch (IOException | RulesParseException e) {
            throw new IllegalStateException("Error parsing default rules", e);
        }

        return rule;
    }

    private static String getDefaultRulesStringFromResources() throws IOException {

        URL url = Resources.getResource(DEFAULT_RULES_RESOURCE_NAME);
        return Resources.toString(url, Charsets.UTF_8);
    }

    public boolean takeMulligan(final Collection<Card> startingHand) {

        Collection<Land> lands = CardUtils.retainAllLandsToArrayList(startingHand);

        _variables.assignValue(VARIABLES.LANDS, lands.size());
        _variables.assignValue(VARIABLES.NONLANDS, startingHand.size() - lands.size());
        _variables.assignValue(VARIABLES.CARDS, startingHand.size());

        _variables.assignSupplier(VARIABLES.LAND_COLORS, () -> CardUtils.getColors(lands));
        _variables.assignSupplier(VARIABLES.CARD_NAMES, () -> CardUtils.getNames(startingHand));

        return _rule.evaluate(_variables);
    }

    public String toFormattedString() {

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

        IntegerAttributeKey LANDS = new IntegerAttributeKey("LANDS");

        IntegerAttributeKey NONLANDS = new IntegerAttributeKey("NONLANDS");

        IntegerAttributeKey CARDS = new IntegerAttributeKey("CARDS");

        ColorsAttributeKey LAND_COLORS = new ColorsAttributeKey("LAND_COLORS");

        StringSetAttributeKey CARD_NAMES = new StringSetAttributeKey("CARD_NAMES");
    }

}
