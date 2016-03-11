package probability.rules;

import java.util.HashMap;
import java.util.Map;

enum Operations {

  AND(new And()), OR(new Or()), EQUALS(new Equals()), LESS_THAN(new LessThan()), GREATER_THAN(
      new GreaterThan());

  private final static Map<String, Operations> NAME_TO_OPERATIONS = new HashMap<>();

  static {
    for (Operations op : Operations.values()) {
      NAME_TO_OPERATIONS.put(op.getSymbol(), op);
    }
  }

  private final Operator _op;

  public Operator getInstance() {
    return _op.getInstance();
  }

  Operations(BinaryOperator op) {
    _op = op;
  }

  public String getSymbol() {
    return _op.getSymbol();
  }

  public static Operations getOperation(String symbol) {
    return NAME_TO_OPERATIONS.get(symbol);
  }

}
