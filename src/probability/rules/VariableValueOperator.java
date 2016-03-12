package probability.rules;

import java.util.Stack;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

abstract class VariableValueOperator implements Operator, Expression, Token {

    private final String _symbol;

    private final int _precedence;

    Variable<?> _variable;

    Object _value;

    VariableValueOperator(String symbol, int precedence) {

        checkNotNull(symbol);
        checkArgument(symbol.length() > 0);
        checkArgument(precedence >= 0);

        _symbol = symbol;
        _precedence = precedence;
    }


    @Override
    public int getPrecedence() {

        return _precedence;
    }

    @Override
    public String getSymbol() {

        return _symbol;
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.OPERATOR;
    }

    private void parse(Variable<?> variable, Token other) throws RulesTokenException {

        _variable = variable;

        if (other instanceof Value.StringValue) {
            _value = _variable.parseValue((Value.StringValue) other);
        } else {
            throw new RulesTokenException("One operand of " + getSymbol() +
                    " must be a value");
        }
    }

    @Override
    public Expression parse(Stack<Token> stack) throws RulesTokenException {

        Token right = stack.pop();
        Token left = stack.pop();

        if (left instanceof Variable<?>) {

            parse((Variable<?>) left, right);
        } else if (right instanceof Variable<?>) {

            parse((Variable<?>) right, left);
        } else {
            throw new RulesTokenException("One operand of " + getSymbol() +
                    " must be a variable");
        }

        return this;
    }


    @Override
    public String toString() {

        return "(" + _variable + " " + getSymbol() + " " + _value + ")";
    }

}
