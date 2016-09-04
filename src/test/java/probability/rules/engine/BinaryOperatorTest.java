package probability.rules.engine;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Stack;

import static probability.rules.engine.TestUtils.createConstantToken;

@RunWith(Parameterized.class)
public class BinaryOperatorTest {

    private final boolean _lhsValue;
    private final boolean _rhsValue;

    @Parameterized.Parameters(name = "{index}: {0} operator {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {false, false},
                {false, true},
                {true, false},
                {true, true}
        });
    }

    public BinaryOperatorTest(boolean lhsValue, boolean rhsValue) {
        _lhsValue = lhsValue;
        _rhsValue = rhsValue;
    }

    private Expression createBinaryOperatorExpression(Operation operation) {

        Operator op = operation.newInstance();
        Assert.assertTrue(op instanceof BinaryOperator);

        Stack<Token> stack = new Stack<>();
        stack.push(createConstantToken(_lhsValue));
        stack.push(createConstantToken(_rhsValue));

        Expression expression = null;
        try {
            expression = op.parse(stack);
        } catch (Token.RulesTokenException e) {
            Assert.fail(e.getMessage());
        }

        return expression;
    }

    @Test
    public void testAnd() {

        Expression expr = createBinaryOperatorExpression(Operation.AND);

        Assert.assertEquals(_lhsValue & _rhsValue, expr.interpret(null));
    }

    @Test
    public void testOr() {

        Expression expr = createBinaryOperatorExpression(Operation.OR);

        Assert.assertEquals(_lhsValue | _rhsValue, expr.interpret(null));
    }
}