package probability.core;

import java.util.EnumMap;

public class EnumCount<K extends Enum<K>> {

    private final EnumMap<K, MutableInteger> map;

    private int totalCount = 0;

    public EnumCount(Class<K> keyType) {
        map = new EnumMap<>(keyType);
    }

    public EnumCount(EnumCount<K> c) {


        map = new EnumMap<>(c.map);
        totalCount = c.totalCount;
    }

    /**
     * Increases the count for {@code key} and returns the new value.
     *
     * @param key the key whose count should be increased
     * @return the count after the value has been increased
     */
    public int increase(K key) {

        MutableInteger entry = map.get(key);

        if (entry == null) {

            map.put(key, new MutableInteger(1));
            totalCount++;

            return 1;
        }

        totalCount++;
        return entry.inc();
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
    }

}
