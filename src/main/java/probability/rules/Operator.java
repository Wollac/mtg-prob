package probability.rules;

/**
 * An Operator is a special {@linkplain Token} that has an identifier and a precedence which is
 * taken into account when parsing infix notation.
 */
interface Operator extends Token {

    /**
     * Returns the precedence of the operator. A lower value corresponds to prioritized operators.
     */
    int getPrecedence();

    /**
     * Returns the symbol that identifies the operator in the string.
     */
    String getSymbol();

    String getProductionRule();

}
