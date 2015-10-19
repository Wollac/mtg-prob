package probability.rules;

public interface Operator extends Token {
  
  public String getSymbol();

  public Operator getInstance();

  public int getPrecedence();
}
