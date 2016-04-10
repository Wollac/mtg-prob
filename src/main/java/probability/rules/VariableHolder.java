package probability.rules;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import probability.attr.AttributeKey;
import probability.attr.ImmutableAttributeHolder;
import probability.attr.SuppliedAttributeHolder;

public final class VariableHolder {

    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]\\w*");

    private final Map<String, Variable<?>> _name2var = new HashMap<>();

    private final SuppliedAttributeHolder _bindings = new SuppliedAttributeHolder();

    public void registerVariables(Iterable<? extends AttributeKey<?>> keys) {

        for (AttributeKey<?> key : keys) {
            registerVariable(key);
        }
    }

    <T> void registerVariable(AttributeKey<T> key) {

        String name = key.getName();

        checkName(name);
        if (isRegistered(key)) {
            throw new IllegalArgumentException("A variable for that key has already been registered");
        }

        _name2var.put(name, Variable.createVariable(key));
        _bindings.putAttributeValue(key, key.getDefaultValue());
    }

    private boolean isRegistered(AttributeKey<?> key) {

        for (Variable<?> var : _name2var.values()) {
            if (var.getAttributeKey().equals(key))
                return true;
        }
        return false;
    }

    boolean isRegistered(String name) {
        return _name2var.containsKey(name);
    }

    public <T> void assignValue(AttributeKey<T> key, T value) {

        _bindings.putAttributeValue(key, value);
    }

    public <T> void assignSupplier(AttributeKey<T> key, Supplier<T> supplier) {

        _bindings.putAttributeSupplier(key, supplier);
    }

    ImmutableAttributeHolder getBindings() {
        return _bindings;
    }

    Variable<?> getVariable(String name) {
        return _name2var.get(name);
    }

    private void checkName(String name) {

        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(
                    "Invalid variable name \"" + name + "\": All names must match " + NAME_PATTERN.pattern());
        }

        if (_name2var.containsKey(name)) {
            throw new IllegalArgumentException(
                    "Invalid variable name \"" + name + "\": already registered");
        }

        if (Operation.getOperationFromSymbol(name) != null) {
            throw new IllegalArgumentException(
                    "Invalid variable name \"" + name + "\": name of an operation");
        }
    }

}
