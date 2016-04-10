package probability.attr;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import probability.utils.Suppliers;

public final class SuppliedAttributeHolder implements ImmutableAttributeHolder {

    private final Map<AttributeKey<?>, Supplier<?>> _supplierMap = new HashMap<>();

    public <T> void setAttributeValue(AttributeKey<T> key, T value) {

        key.checkValid(value);
        setAttributeValueUnchecked(key, value);
    }

    public <T> void setAttributeValueUnchecked(AttributeKey<T> key, T value) {

        _supplierMap.put(key, () -> value);
    }

    public <T> void putAttributeSupplier(AttributeKey<T> key, Supplier<T> supplier) {

        // always memoize the supplier
        _supplierMap.put(key, Suppliers.memoize(supplier));
    }

    @Override
    public <T> T getAttributeValue(AttributeKey<T> key, T def) {

        Supplier<?> supplier = _supplierMap.get(key);
        if (supplier == null) {
            return def;
        }

        @SuppressWarnings("unchecked")
        T value = (T) supplier.get();

        return value;
    }

    @Override
    public <T> T getAttributeValue(AttributeKey<T> key) {
        return getAttributeValue(key, key.getDefaultValue());
    }

}
