package probability.core;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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
import probability.config.Settings;
import probability.messages.Messages;
import probability.rules.Rule;
import probability.rules.RuleLoader;
import probability.rules.RuleLoader.RulesParseException;
import probability.rules.VariableHolder;
import probability.utils.FormattedPrintWriter;
import probability.utils.ReadOrWriteDefaultIO;
import probability.utils.Suppliers;

public class MulliganRule extends ReadOrWriteDefaultIO {

    private static final String DEFAULT_RULES_RESOURCE_NAME = "default_mulligan_rule.txt";

    private static final ResourceBundle bundle = ResourceBundle.getBundle("variables");

    private static final VariableHolder _variables = createVariableHolder();

    private Rule _rule;

    public MulliganRule(File file) {

        super(file, Settings.CHARSET);

        assert _rule != null;
    }

    private static VariableHolder createVariableHolder() {

        List<AttributeKey<?>> variableKeys = AttributeUtils.getAttributeKeys(VARIABLES.class);

        VariableHolder holder = new VariableHolder();
        holder.registerVariables(variableKeys);

        return holder;
    }

    @Override
    protected boolean read(Reader reader) throws IOException {

        boolean success = false;
        try {
            RuleLoader loader = new RuleLoader(_variables);
            _rule = loader.read(reader);

            if (_rule != null) {
                success = true;
            }
        } catch (RulesParseException e) {
            System.err.println("Error parsing rules in line "
                    + e.getErrorLine() + ": " + e.getMessage());
        }

        return success;
    }

    @Override
    protected void writeDefault(Writer writer) throws IOException {

        _rule = getDefaultRule();

        try (FormattedPrintWriter out = new FormattedPrintWriter(writer, 100)) {

            out.setPrefixString("// ");
            printDescription(out);
            out.setPrefixString("");
            out.println();

            _rule.toStrings().forEach(out::println);
        }
    }

    private void printDescription(FormattedPrintWriter writer) {

        writer.printlnTitle(Messages.get().mulliganRuleDescriptionTitle());
        Rule.printGrammar(writer);
        writer.println();
        writer.println(Messages.get().variablesDescriptionTitle());

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
            rule = loader.read(getDefaultRulesReaderFromResources());
        } catch (IOException | RulesParseException e) {
            throw new IllegalStateException("Error parsing default rules", e);
        }

        return rule;
    }

    private static Reader getDefaultRulesReaderFromResources() throws IOException {

        URL url = Resources.getResource(DEFAULT_RULES_RESOURCE_NAME);
        return Resources.asCharSource(url, Charsets.UTF_8).openStream();
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

    private interface VARIABLES {

        IntegerAttributeKey LANDS = new IntegerAttributeKey("LANDS");

        IntegerAttributeKey NONLANDS = new IntegerAttributeKey("NONLANDS");

        IntegerAttributeKey CARDS = new IntegerAttributeKey("CARDS");

        ColorsAttributeKey LAND_COLORS = new ColorsAttributeKey("LAND_COLORS");

        StringSetAttributeKey CARD_NAMES = new StringSetAttributeKey("CARD_NAMES");
    }

}
