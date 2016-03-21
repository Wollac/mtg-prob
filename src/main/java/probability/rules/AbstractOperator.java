package probability.rules;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class contains the common functionality for all {@linkplain Operator}.
 */
abstract class AbstractOperator implements Operator {

    private final String _symbol;

    private final int _precedence;

    /**
     * Creates an Operator.
     *
     * @param symbol     nonempty string used to identify the operator
     * @param precedence precedence value of the operator
     */
    AbstractOperator(String symbol, int precedence) {

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

}
