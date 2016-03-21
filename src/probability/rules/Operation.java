package probability.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Static collection of all the {@linkplain Operator} that can be evaluated.
 */
enum Operation {

    NOT(Not.class), AND(And.class), OR(Or.class), EQUAL(Equal.class), NOT_EQUAL(NotEqual.class),
    LESS_THAN(LessThan.class), GREATER_THAN(GreaterThan.class);

    private static final Map<String, Operation> OPERATIONS_BY_SYMBOL;

    static {
        Operation[] operations = values();

        OPERATIONS_BY_SYMBOL = new HashMap<>(operations.length);
        for (Operation op : operations) {

            String symbol = op.getSymbol();

            if (symbol == null || symbol.isEmpty()) {
                throw new IllegalStateException(op.getType().getName() + " does not provide a nonempty symbol");
            }
            if (OPERATIONS_BY_SYMBOL.containsKey(symbol)) {
                throw new IllegalStateException(symbol + " for " + op.getType().getName() + " is not unique");
            }

            OPERATIONS_BY_SYMBOL.put(symbol, op);
        }
    }

    private final Class<? extends Operator> _op;

    Operation(Class<? extends Operator> op) {
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
    public Operator newInstance() {
        try {
            return _op.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            // this should not happen as long as enum constants are set correctly
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns all the symbols for all the operators.
     */
    public static Collection<String> getAllOperatorSymbols() {
        return Collections.unmodifiableSet(OPERATIONS_BY_SYMBOL.keySet());
    }

    private String getSymbol() {
        // create a new instance and return its symbol
        return newInstance().getSymbol();
    }

    private Class<?> getType() {
        return _op;
    }

}
