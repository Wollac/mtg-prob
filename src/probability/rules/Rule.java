package probability.rules;

import probability.attr.ImmutableAttributeHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A Rule corresponds to multiple {@linkplain Expression} evaluating to true,
 * if at least one expression evaluates to true.
 */
public class Rule {

    private final List<Expression> _expressions;

    Rule(List<Expression> expressions) {

        _expressions = expressions;
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
