package probability.rules;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import probability.attr.AttributeKey;
import probability.attr.ImmutableAttributeHolder;

public class VariableHolder {

    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]\\w*");

    private final Map<String, Variable<?>> _name2var = new HashMap<>();

    private final SuppliedAttributeHolder _bindings = new SuppliedAttributeHolder();

    public void registerVariables(Iterable<? extends AttributeKey<?>> keys) {

        for (AttributeKey<?> key : keys) {
            registerVariable(key);
        }
    }

    public <T> void registerVariable(AttributeKey<T> key) {
        registerVariable(key, key.getName());
    }

    public <T> void registerVariable(AttributeKey<T> key, String name) {

        checkName(name);
        if (isRegistered(key)) {
            throw new IllegalArgumentException("A variable for that key has already been registered");
        }

        _name2var.put(name, Variable.createVariable(key));
        _bindings.putAttributeValue(key, key.getDefaultValue());
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

    private static final class SuppliedAttributeHolder implements ImmutableAttributeHolder {

        private final Map<AttributeKey<?>, Supplier<?>> _supplierMap = new HashMap<>();

        public <T> void putAttributeValue(AttributeKey<T> key, T value) {

            _supplierMap.put(key, () -> value);
        }

        public <T> void putAttributeSupplier(AttributeKey<T> key, Supplier<T> supplier) {

            _supplierMap.put(key, Suppliers.memoize(supplier));
        }

        @Override
        public <T> T getAttributeValue(AttributeKey<T> key, T def) {

            if (_supplierMap.containsKey(key)) {
                return getAttributeValue(key);
            }

            return def;
        }

        @Override
        public <T> T getAttributeValue(AttributeKey<T> key) {

            Supplier<?> supplier = _supplierMap.get(key);

            // the variable holder assures that there are no variables without a supplier
            assert supplier != null;

            @SuppressWarnings("unchecked")
            T value = (T) supplier.get();

            return value;
        }
    }

}
