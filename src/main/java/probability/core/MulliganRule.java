package probability.core;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import probability.attr.AttributeKey;
import probability.attr.AttributeUtils;
import probability.attr.ColorsAttributeKey;
import probability.attr.IntegerAttributeKey;
import probability.attr.StringSetAttributeKey;
import probability.rules.Rule;
import probability.rules.RuleLoader;
import probability.rules.RuleLoader.RulesParseException;
import probability.rules.VariableHolder;
import probability.utils.FormattedPrintWriter;
import probability.utils.Messages;
import probability.utils.Suppliers;

public class MulliganRule {

    private static final String DEFAULT_RULES_RESOURCE_NAME = "default_mulligan_rule.txt";

    private static final ResourceBundle bundle = ResourceBundle.getBundle("variables");

    private final Rule _rule;

    private final VariableHolder _variables = new VariableHolder();

    public MulliganRule(File file) {

        registerVariables();
        _rule = loadRule(file);
    }

    private Rule loadRule(File file) {

        Rule rule = loadFromFile(file);
        if (rule == null) {
            rule = getDefaultRule();
            writeRuleToFile(rule, file);
        }

        return rule;
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

        return null;
    }

    private void writeRuleToFile(Rule rule, File file) {

        if (!file.exists()) {

            System.err.println("Mulligan rules file " + file + " does not exist," +
                    " creating new file with description an default rules");

            try (FormattedPrintWriter out = new FormattedPrintWriter(Files.newWriter(file, Charsets.UTF_8), 100)) {

                out.setPrefixString("// ");
                printDescription(out);
                out.setPrefixString("");
                out.println();

                rule.toStrings().forEach(out::println);
            } catch (IOException e) {
                System.err.println("Could not write default mulligan rules file: " + e.getMessage());
            }
        }
    }

    private void printDescription(FormattedPrintWriter writer) {

        writer.printlnTitle(Messages.formatMessage(MessageKeys.MULLIGAN));
        Rule.printGrammar(writer);
        writer.println();
        writer.println(Messages.formatMessage(MessageKeys.VARIABLES));

        writer.setIndentionLevel(1);
        for (AttributeKey<?> var : AttributeUtils.getAttributeKeys(VARIABLES.class)) {
            writer.println(getVariableDescription(var));
        }
        writer.setIndentionLevel(0);
    }

    private static String getVariableDescription(AttributeKey<?> var) {

        try {
            return var.getName() + ": " + bundle.getString(var.getName());
        } catch (MissingResourceException e) {
            return var.getName();
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

    public boolean takeMulligan(final Collection<CardObject> startingHand) {

        _variables.assignValue(VARIABLES.CARDS, startingHand.size());

        Supplier<Integer> landsSupplier = Suppliers.memoize(() -> CardUtils.getNumberOfLandObjects(startingHand));

        _variables.assignSupplier(VARIABLES.LANDS, landsSupplier);
        _variables.assignSupplier(VARIABLES.NONLANDS, () -> startingHand.size() - landsSupplier.get());

        _variables.assignSupplier(VARIABLES.LAND_COLORS, () -> CardUtils.getLandColors(startingHand));
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
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    private enum MessageKeys implements Messages.MessageKey {

        MULLIGAN("rules.description.mulligan"), VARIABLES("rules.description.variables");

        private final String _key;

        MessageKeys(String key) {
            _key = key;
        }

        @Override
        public String getBundleKey() {
            return _key;
        }
    }

    private interface VARIABLES {

        IntegerAttributeKey LANDS = new IntegerAttributeKey("LANDS");

        IntegerAttributeKey NONLANDS = new IntegerAttributeKey("NONLANDS");

        IntegerAttributeKey CARDS = new IntegerAttributeKey("CARDS");

        ColorsAttributeKey LAND_COLORS = new ColorsAttributeKey("LAND_COLORS");

        StringSetAttributeKey CARD_NAMES = new StringSetAttributeKey("CARD_NAMES");
    }

}
