package probability.rules;

import probability.attr.AttributeHolder;
import probability.attr.AttributeKey;
import probability.attr.ImmutableAttributeHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class VariableHolder {

    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]\\w*");

    private final Map<String, Variable<?>> _name2var = new HashMap<>();

    private final AttributeHolder _bindings = new AttributeHolder();

    public <T> void registerVariable(AttributeKey<T> key) {
        registerVariable(key, key.getName());
    }

    public <T> void registerVariable(AttributeKey<T> key, String name) {

        checkName(name);
        if (isRegistered(key)) {
            throw new IllegalArgumentException("A variable for that key has already been registered");
        }

        _name2var.put(name, Variable.createVariable(key));
    }

    public <T> void registerAndAssign(AttributeKey<T> key, T value) {
        registerVariable(key);
        assignValue(key, value);
    }

    public boolean isRegistered(AttributeKey<?> key) {

        for (Variable<?> var : _name2var.values()) {
            if (var.getAttributeKey().equals(key))
                return true;
        }
        return false;
    }

    public boolean isRegistered(String name) {
        return _name2var.containsKey(name);
    }

    public <T> void assignValue(AttributeKey<T> key, T value) {

        _bindings.setAttributeValueUnchecked(key, value);
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
                    "Invalid variable name: All names must match " + NAME_PATTERN);
        }

        if (_name2var.containsKey(name)) {
            throw new IllegalArgumentException(
                    "Invalid variable name: " + name + " has already been registered");
        }

        if (Operation.getOperationFromSymbol(name) != null) {
            throw new IllegalArgumentException(
                    "Invalid variable name: " + name + " is the name of an operation");
        }
    }

}
