package probability.rules;

import java.util.Stack;

abstract class UnparsableToken implements Token {

    @Override
    public Expression parse(Stack<Token> stack) throws RulesTokenException {
        throw new RulesTokenException(getClass().getName() + " cannot be parsed into an expression");
    }

}
