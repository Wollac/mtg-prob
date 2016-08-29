package probability.core;

import com.google.common.io.Resources;

import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import probability.attr.AttributeKey;
import probability.attr.AttributeUtils;
import probability.attr.ColorsAttributeKey;
import probability.attr.IntegerAttributeKey;
import probability.attr.StringSetAttributeKey;
import probability.config.Settings;
import probability.messages.Messages;
import probability.messages.ProjectException;
import probability.rules.Rule;
import probability.rules.RuleLoader;
import probability.rules.RuleLoader.RulesParseException;
import probability.rules.VariableHolder;
import probability.utils.FormattedPrintWriter;
import probability.utils.ReadOrWriteDefaultIO;
import probability.utils.Suppliers;

public class MulliganRule {

    private static final String DEFAULT_RULES_RESOURCE_NAME = "default_mulligan_rule.txt";

    private static final ResourceBundle bundle = ResourceBundle.getBundle("variables");

    private static final VariableHolder _variables = createVariableHolder();

    private static final String FILE_TYPE = "mulligan";

    private Rule _rule = null;

    public MulliganRule(File file) throws ProjectException {

        readRule(Objects.requireNonNull(file));

        if (_rule == null) {
            throw new ProjectException(ProjectException.ProjectError.INVALID_MULLIGAN_RULE);
        }
    }

    private static VariableHolder createVariableHolder() {

        List<AttributeKey<?>> variableKeys = AttributeUtils.getAttributeKeys(VARIABLES.class);

        Logger.debug("registerVariables={}", variableKeys);

        VariableHolder holder = new VariableHolder();
        holder.registerVariables(variableKeys);

        return holder;
    }

    private static String getVariableDescription(AttributeKey<?> var) {

        try {
            return var.getName() + ": " + bundle.getString(var.getName());
        } catch (MissingResourceException e) {
            return var.getName();
        }
    }

    private static Reader getDefaultRulesReaderFromResources() throws IOException {

        URL url = Resources.getResource(DEFAULT_RULES_RESOURCE_NAME);
        return getResourceReader(url, Settings.CHARSET);
    }

    private static Reader getResourceReader(URL url, Charset charset) throws IOException {

        Logger.debug("reader url={}, charset={}", url, charset);

        return Resources.asCharSource(url, charset).openStream();
    }

    private void readRule(File file) {

        Logger.debug("readingFile={}", file);

        MulliganIO io = new MulliganIO(file);
        _rule = io.getRule();
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

    private class MulliganIO extends ReadOrWriteDefaultIO {

        private Rule _rule = null;

        private MulliganIO(File file) {
            super(file, Settings.CHARSET);
        }

        @Override
        protected void read(Reader reader) throws IOException {

            try {
                RuleLoader loader = new RuleLoader(_variables);
                _rule = loader.read(reader);
            } catch (RulesParseException e) {
                Logger.error(Messages.get().parseFileExceptionWithLineNumber(getFileName(), FILE_TYPE,
                        e.getLocalizedMessage(), e.getErrorLine()));
                Logger.debug(e);
            }
        }

        @Override
        protected void writeDefault(Writer writer) throws IOException {

            _rule = getDefaultRule();

            Logger.warn(Messages.get().writeDefaultFile(getFileName(), FILE_TYPE));

            try (FormattedPrintWriter out = new FormattedPrintWriter(writer, Settings.LINE_WIDTH)) {

                out.setPrefixString("// ");
                printDescription(out);
                out.setPrefixString("");
                out.println();

                _rule.toStrings().forEach(out::println);
            }
        }

        private Rule getRule() {

            try {
                readOrWriteDefault();
            } catch (IOException e) {
                Logger.error(Messages.get().readFileException(getFileName(), FILE_TYPE,
                        e.getLocalizedMessage()));
                Logger.debug(e);
            }

            return _rule;
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
