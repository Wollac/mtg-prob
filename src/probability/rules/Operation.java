package probability.rules;

import java.util.HashMap;
import java.util.Map;

/**
 * Static collection of all the {@linkplain Operator} that can be evaluated.
 */
enum Operation {

    NOT(new Not()), AND(new And()), OR(new Or()), EQUAL(new Equal()), NOT_EQUAL(new NotEqual()),
    LESS_THAN(new LessThan()), GREATER_THAN(new GreaterThan());

    private final static Map<String, Operation> OPERATIONS_BY_SYMBOL;

    static {
        Operation[] operations = values();

        OPERATIONS_BY_SYMBOL = new HashMap<>(operations.length);
        for (Operation op : operations) {
            OPERATIONS_BY_SYMBOL.put(op.getSymbol(), op);
        }
    }

    private final Operator _op;

    Operation(Operator op) {
        _op = op;
    }

    /**
     * Returns the operation corresponding to the given symbol.
     *
     * @return the operation, or {@code null} if there is no operation with that symbol
     */
    public static Operation getOperationFromSymbol(String symbol) {
        return OPERATIONS_BY_SYMBOL.get(symbol);
    }

    /**
     * Returns an instance of the corresponding operator.
     */
    public Operator getOperatorInstance() {
        return _op.getInstance();
    }

    /**
     * Returns the symbol of the corresponding operator.
     */
    public String getSymbol() {
        return _op.getSymbol();
    }

}
