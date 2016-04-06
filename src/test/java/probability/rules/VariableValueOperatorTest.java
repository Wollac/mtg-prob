package probability.rules;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import probability.attr.AttributeKey;
import probability.attr.ImmutableAttributeHolder;
import probability.attr.IntegerAttributeKey;

import java.util.Arrays;
import java.util.Collection;
import java.util.Stack;
import java.util.function.BiPredicate;

@RunWith(Parameterized.class)
public class VariableValueOperatorTest {

    private static final IntegerAttributeKey KEY = new IntegerAttributeKey("INTEGER");
    private final int _varBinding;
    private final int _value;

    public VariableValueOperatorTest(int varBinding, int value) {
        _varBinding = varBinding;
        _value = value;
    }

    private static void testOperation(int varBinding, int value, Operation operation, BiPredicate<Integer, Integer> expected) {
        assertOperation(KEY, varBinding, value, operation, expected);
    }

    private static <T> void assertOperation(AttributeKey<T> key, T varBinding, T value, Operation operation, BiPredicate<T, T> expected) {

        Expression expr = createOperationExpression(operation, key, value.toString());
        ImmutableAttributeHolder binding = createSingleVariableBinding(key, varBinding);

        Assert.assertEquals(expected.test(varBinding, value), expr.interpret(binding));
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

    @Parameters(name = "{index}: var({0}) operator {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0, 0}, {0, 1}, {1, 0}, {1, 1}
        });
    }

    @Test
    public void testEqual() {
        testOperation(_varBinding, _value, Operation.EQUAL, Integer::equals);
    }

    @Test
    public void testNotEqual() {
        testOperation(_varBinding, _value, Operation.NOT_EQUAL, (x, y) -> !x.equals(y));
    }

    @Test
    public void testLessThan() {
        testOperation(_varBinding, _value, Operation.LESS_THAN, (x, y) -> x.compareTo(y) < 0);
    }

    @Test
    public void testGreaterThan() {
        testOperation(_varBinding, _value, Operation.GREATER_THAN, (x, y) -> x.compareTo(y) > 0);
    }

}