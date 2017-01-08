package probability.rules.engine;

import static probability.rules.engine.TestUtils.createConstantToken;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Stack;

@RunWith(Parameterized.class) public class UnaryOperatorTest {

  private final boolean _value;

  @Parameterized.Parameters(name = "{index}: operator {0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {{true}, {false}});
  }

  public UnaryOperatorTest(boolean value) {
    _value = value;
  }

  private Expression createUnaryOperatorExpression(Operation operation) {

    Operator op = operation.newInstance();
    Assert.assertTrue(op instanceof UnaryOperator);

    Stack<Token> stack = new Stack<>();
    stack.push(createConstantToken(_value));

    Expression expression = null;
    try {
      expression = op.parse(stack);
    } catch (Token.RulesTokenException e) {
      Assert.fail(e.getMessage());
    }

    return expression;
  }

  @Test public void testNot() {

    Expression expr = createUnaryOperatorExpression(Operation.NOT);

    Assert.assertEquals(!_value, expr.interpret(null));
  }
}
