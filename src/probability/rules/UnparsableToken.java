package probability.rules;

import java.util.Stack;

/**
 * A special kind of token that occurs in expression but cannot be parsed on its own.
 */
abstract class UnparsableToken implements Token {

    @Override
    public Expression parse(Stack<Token> stack) throws RulesTokenException {
        throw new RulesTokenException(getClass().getName() + " cannot be parsed into an expression");
    }

}
