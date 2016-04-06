package probability.rules;

import probability.attr.AttributeKey;
import probability.attr.ImmutableAttributeHolder;

import java.util.Stack;

class TestUtils {

    private TestUtils() {
        // do not initialize
    }

    static <T> ImmutableAttributeHolder createSingleVariableBinding(AttributeKey<T> key, T varBinding) {

        VariableHolder holder = new VariableHolder();
        holder.registerVariable(key);
        holder.assignValue(key, varBinding);

        return holder.getBindings();
    }

    static Expression createVariableValueOperatorExpression(Operation operation, AttributeKey<?> key, String valueString) throws Token.RulesTokenException {

        Variable<?> var = new Variable<>(key);

        Stack<Token> stack = new Stack<>();
        stack.push(var);
        stack.push(new Value.StringValue(valueString));

        return operation.newInstance().parse(stack);
    }
}
