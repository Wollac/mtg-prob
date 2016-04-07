package probability.utils;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.IntFunction;

/**
 * A simple cache for mapping integers in a fixed interval to certain values.
 * {@link FixedCapacityIntCache} is functional equivalent to
 * {@linkplain java.util.Map#computeIfAbsent}, but can be implemented using
 * Arrays when an upper bound on the keys is known.
 *
 * @param <V> the type of mapped values
 */
public final class FixedCapacityIntCache<V> {

    private final ArrayList<V> _list;

    private final IntFunction<? extends V> _mappingFunction;

    /**
     * Constructs a new cache, where new values for keys are computed using
     * the provided mapping function.
     *
     * @param capacity        limits the range of feasible keys to an integer
     *                        in the interval [0,capacity)
     * @param mappingFunction the function to compute a value
     * @throws IllegalArgumentException if {@code capacity <= 0}
     * @throws NullPointerException     if the mapping function is {@code null}
     */
    public FixedCapacityIntCache(int capacity, IntFunction<? extends V> mappingFunction) {

        Preconditions.checkArgument(capacity > 0, capacity);

        _list = new ArrayList<>(Collections.nCopies(capacity, null));

        _mappingFunction = Preconditions.checkNotNull(mappingFunction);
    }

    /**
     * Returns the upper bound for any key.
     */
    public int capacity() {
        return _list.size();
    }

    /**
     * If the specified integer key is not already associated with a value (or
     * is mapped to {@code null}), attempts to compute its value using the
     * mapping function provided in the constructor and enters the result into
     * this cache unless {@code null}.
     *
     * @param key key with which the specified value is to be associated
     * @return the current (existing or computed) value associated with
     * the specified key, or null if the computed value is null
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   {@code (key < 0 || index >= capacity())}
     */
    public V get(int key) {

        V v = _list.get(key);
        if (v == null) {
            V newValue = _mappingFunction.apply(key);
            if (newValue != null) {
                _list.add(key, newValue);
                return newValue;
            }
        }

        return v;
    }
}
