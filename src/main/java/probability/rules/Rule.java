package probability.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import probability.attr.ImmutableAttributeHolder;
import probability.messages.Messages;
import probability.utils.FormattedPrintWriter;

import static com.google.common.base.Preconditions.checkNotNull;
import static probability.rules.NamingConventions.EXPRESSION;
import static probability.rules.NamingConventions.STRING;
import static probability.rules.NamingConventions.VARIABLE;

/**
 * A Rule corresponds to multiple {@linkplain Expression} evaluating to true, if at least one
 * expression evaluates to true.
 */
public class Rule {

    private final List<Expression> _expressions;

    /**
     * Creates a rule from a list of expressions.
     */
    Rule(List<Expression> expressions) {

        _expressions = checkNotNull(expressions);
    }

    public static void printGrammar(FormattedPrintWriter writer) {

        writer.println(Messages.get().grammarDescriptionRule());
        writer.println(Messages.get().grammarDescriptionExpression(EXPRESSION));

        writer.setIndentionLevel(1);

        writer.println(Parentheses.getProductionRules());

        for (Operation o : Operation.values()) {
            writer.println(o.getProductionRule());
        }

        writer.setIndentionLevel(0);

        writer.println(Messages.get().grammarDescriptionVariable(VARIABLE));
        writer.println(Messages.get().grammarDescriptionString(STRING, StringTokenizer.QUOTE_CHAR));
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
