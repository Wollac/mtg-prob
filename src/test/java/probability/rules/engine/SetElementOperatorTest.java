package probability.rules.engine;

import com.google.common.collect.Sets;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiPredicate;

import probability.attr.ImmutableAttributeHolder;
import probability.attr.StringSetAttributeKey;

import static probability.rules.engine.TestUtils.createSingleVariableBinding;
import static probability.rules.engine.TestUtils.createVariableValueOperatorExpression;

@RunWith(Parameterized.class)
public class SetElementOperatorTest {

    private static final StringSetAttributeKey KEY = new StringSetAttributeKey("SET");
    private final Set<String> _varBinding;
    private final String _value;

    @Parameterized.Parameters(name = "{index}: var({0}) operator {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Sets.newHashSet("A", ""), ""},
                {Sets.newHashSet("A", "B"), "A"},
                {Sets.newHashSet("A", "B"), "[A, B]"},
                {Collections.emptySet(), ""},
                {Collections.emptySet(), "[]"},
                {Sets.newHashSet("A"), "B"}
        });
    }

    public SetElementOperatorTest(Set<String> varBinding, String value) {
        _varBinding = varBinding;
        _value = value;
    }

    private static void testOperation(Set<String> varBinding, String value, Operation operation, BiPredicate<Set<String>, String> expected) {

        Expression expr;
        try {
            expr = createVariableValueOperatorExpression(operation, KEY, value);
        } catch (Token.RulesTokenException e) {
            assertValidException(value, e);
            return;
        }

        ImmutableAttributeHolder binding = createSingleVariableBinding(KEY, varBinding);

        Assert.assertEquals(expected.test(varBinding, value), expr.interpret(binding));
    }

    private static void assertValidException(String value, Token.RulesTokenException caughtException) {

        try {
            if (KEY.parseValue(value).size() == 1) {
                Assert.fail(caughtException.getMessage());
            }
        } catch (Exception e) {
            Assert.fail(caughtException.getMessage());
        }
    }

    @Test
    public void testContains() {
        testOperation(_varBinding, _value, Operation.CONTAINS, Set::contains);
    }
}