package probability.core;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class EnumCount<K extends Enum<K>> {

    private final EnumMap<K, MutableInteger> map;

    private int totalCount = 0;

    public EnumCount(Class<K> keyType) {
        map = new EnumMap<>(keyType);
    }

    public EnumCount(Class<K> keyType, EnumCount<K> c) {

        this(keyType);

        for (Map.Entry<K, MutableInteger> entry : c.map.entrySet()) {
            increase(entry.getKey(), entry.getValue().get());
        }
    }

    /**
     * Increases the count for {@code key} by one and returns the new value.
     *
     * @param key the key whose count should be increased
     * @return the count after the value has been increased
     */
    public int increase(K key) {
        return increase(key, 1);
    }

    /**
     * Increases the count for {@code key} by the specified amount and returns
     * the new total value.
     *
     * @param key the key whose count should be increased
     * @param val the amount by which the key should be increased
     * @return the count after the value has been increased
     */
    private int increase(K key, int val) {

        MutableInteger entry = map.get(key);

        if (entry == null) {

            map.put(key, new MutableInteger(val));
            totalCount++;

            return val;
        }

        totalCount += val;
        return entry.inc();
    }

    /**
     * Increases the count for each key in {@code keys} by one.
     *
     * @param keys the keys whose counts should be increased
     */
    public void increaseEach(Iterable<K> keys) {

        for (K key : keys) {
            increase(key);
        }
    }

    /**
     * Decreases the count if possible if possible and returns the old count value.
     *
     * @param key the key whose count should be decreased
     * @return the count after decreasing, or -1 if the count has not been decreased
     */
    public int decrease(K key) {

        MutableInteger entry = map.get(key);

        if (entry == null) {
            return -1;
        }

        if (entry.get() == 0) {
            return -1;
        }

        totalCount--;
        return entry.dec();
    }

    public int count(K key) {

        MutableInteger entry = map.get(key);

        if (entry == null) {
            return 0;
        }

        return entry.get();
    }

    public boolean contains(K key) {
        return count(key) > 0;
    }

    public int totalCount() {

        return totalCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EnumCount)) {
            return false;
        }

        EnumCount other = (EnumCount) obj;
        return totalCount == other.totalCount && Objects.equals(map, other.map);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(map);
    }

    private static final class MutableInteger {

        private int _val;

        public MutableInteger(int val) {
            _val = val;
        }

        public int get() {
            return _val;
        }

        public int inc() {
            return ++_val;
        }

        public int dec() {
            return --_val;
        }

        @Override
        public int hashCode() {
            return _val;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof MutableInteger && ((MutableInteger) obj)._val == _val;
        }

    }

}
