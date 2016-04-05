package probability.rules;

import org.junit.Assert;
import org.junit.Test;
import probability.attr.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Stack;

public class VariableValueOperatorTest {

    private static <T> void assertEqual(AttributeKey<T> key, T varBinding, T value) {

        Expression expr = createOperationExpression(Operation.EQUAL, key, value.toString());
        ImmutableAttributeHolder binding = createSingleVariableBinding(key, varBinding);

        Assert.assertEquals(varBinding.equals(value), expr.interpret(binding));
    }

    private static <T> ImmutableAttributeHolder createSingleVariableBinding(AttributeKey<T> key, T varBinding) {

        VariableHolder holder = new VariableHolder();
        holder.registerVariable(key);
        holder.assignValue(key, varBinding);

        return holder.getBindings();
    }

    private static Expression createOperationExpression(Operation operation, AttributeKey<?> key, String valueString) {

        Variable<?> var = new Variable<>(key);

        Stack<Token> stack = new Stack<>();
        stack.push(var);
        stack.push(new Value.StringValue(valueString));

        Expression expression = null;
        try {
            expression = operation.newInstance().parse(stack);
        } catch (Token.RulesTokenException e) {
            Assert.fail(e.getMessage());
        }

        return expression;
    }

    @Test
    public void testIntegerEquals() {
        IntegerAttributeKey key = new IntegerAttributeKey("INTEGER");

        assertEqual(key, 0, 1);
        assertEqual(key, 1, 0);
        assertEqual(key, 1, 1);
    }

    @Test
    public void testStringEqual() {

        StringAttributeKey key = new StringAttributeKey("STRING");

        assertEqual(key, "A", "B");
        assertEqual(key, "a", "A");
        assertEqual(key, "A", "A");
    }

    @Test
    public void testSetEqual() {

        StringSetAttributeKey key = new StringSetAttributeKey("SET");

        assertEqual(key, Collections.singleton("A"), Collections.emptySet());
        assertEqual(key, new HashSet<>(Arrays.asList("A", "B")), new HashSet<>(Arrays.asList("B", "A")));
    }
}