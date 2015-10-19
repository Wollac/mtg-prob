package probability.rules;

public interface Operator extends Expression {
  
  public String getSymbol();

  public Operator getInstance();

  public int getPrecedence();
}
