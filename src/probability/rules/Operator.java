package probability.rules;

public interface Operator extends Token {

    String getSymbol();

    Operator getInstance();

    int getPrecedence();
}
