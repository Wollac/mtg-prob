package probability.rules;

import org.junit.Assert;

import java.util.Stack;

import probability.attr.AttributeKey;
import probability.attr.ImmutableAttributeHolder;

class TestUtils {

    private static final Tautology TAUTOLOGY = new Tautology();
    private static final Contradiction CONTRADICTION = new Contradiction();

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

        Operator op = operation.newInstance();
        Assert.assertTrue(op instanceof VariableValueOperator);

        Variable<?> var = new Variable<>(key);

        Stack<Token> stack = new Stack<>();
        stack.push(var);
        stack.push(new Value.StringValue(valueString));

        return op.parse(stack);
    }

    static Token createConstantToken(boolean value) {

        if (value) {
            return TAUTOLOGY;
        }
        return CONTRADICTION;
    }

    private static class Tautology implements Token, Expression {

        @Override
        public TokenType getTokenType() {
            return TokenType.FUNCTION;
        }

        @Override
        public Expression parse(Stack<Token> stack) throws RulesTokenException {
            return this;
        }

        @Override
        public boolean interpret(ImmutableAttributeHolder bindings) {
            return true;
        }
    }

    private static class Contradiction implements Token, Expression {

        @Override
        public TokenType getTokenType() {
            return TokenType.FUNCTION;
        }

        @Override
        public Expression parse(Stack<Token> stack) throws RulesTokenException {
            return this;
        }

        @Override
        public boolean interpret(ImmutableAttributeHolder bindings) {
            return false;
        }
    }
}
