package probability.rules;

import com.google.common.base.Supplier;
import probability.attr.ImmutableAttributeHolder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.checkNotNull;
import static probability.rules.NamingConventions.EXPRESSION;
import static probability.rules.NamingConventions.STRING;

/**
 * A Rule corresponds to multiple {@linkplain Expression} evaluating to true, if at least one
 * expression evaluates to true.
 */
public class Rule {

    private enum RuleDescriptionMessages implements Supplier<String> {
        RULE("rules.description.rule"), EXPRESSION("rules.description.expression"), STRING("rules.description.string");

        private String _key;

        RuleDescriptionMessages(String resourceKey) {
            _key = resourceKey;
        }

        @Override
        public String get() {
            return _key;
        }
    }

    public static String formatMessage(RuleDescriptionMessages messageKey,
                                       Object... arguments) {

        String pattern = bundle.getString(messageKey.get());
        return MessageFormat.format(pattern, arguments);
    }

    private static final ResourceBundle bundle = ResourceBundle.getBundle("rules");

    private final List<Expression> _expressions;

    /**
     * Creates a rule from a list of expressions.
     */
    Rule(List<Expression> expressions) {

        _expressions = checkNotNull(expressions);
    }

    public static void printGrammar() {

        System.out.println(formatMessage(RuleDescriptionMessages.EXPRESSION, EXPRESSION));

        System.out.println("  " + Parentheses.getProductionRules());

        for (Operation o : Operation.values()) {
            System.out.println("  " + o.getProductionRule());
        }

        System.out.println(formatMessage(RuleDescriptionMessages.STRING, STRING, StringTokenizer.QUOTE_CHAR));
    }

    /**
     * Evaluates a rules with respect to variable bindings.
     */
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

    /**
     * Converts each expression of the rule to its corresponding string.
     */
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
