package probability.attr;

import java.util.function.Predicate;

public abstract class AttributeKey<T> {

    final private String _name;

    final private T _defaultValue;
    final private Predicate<T> _validator;
    private Class<? super T> _type;

    AttributeKey(String name, Class<? super T> type, T defaultValue, Predicate<T> validator) {
        _name = name;
        _defaultValue = defaultValue;
        _type = type;
        _validator = validator;
    }

    AttributeKey(String name, Class<? super T> type, T defaultValue) {

        // initialize with tautology
        this(name, type, defaultValue, t -> true);
    }

    public final String getName() {
        return _name;
    }

    public final T getDefaultValue() {
        return _defaultValue;
    }

    public final Class<? super T> getValueType() {
        return _type;
    }

    public final boolean isValid(T value) {
        return _validator.test(value);
    }

    public final void checkValid(T value) throws IllegalArgumentException {

        if (!isValid(value)) {
            throw new IllegalArgumentException(value + " is an invalid value for attribute "
                    + getName());
        }
    }

    public abstract T parseValue(String valueString) throws AttributeParseException;

    @Override
    public String toString() {
        return getName();
    }

    public static class AttributeParseException extends Exception {

        private static final long serialVersionUID = 1L;

        public AttributeParseException(String s, AttributeKey<?> attribute) {
            super("attribute " + attribute.getName() + ": " + s);
        }

    }

}
