package probability.utils;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class EnumCount<K extends Enum<K>> {

  private final EnumMap<K, MutableInteger> _map;

  private int _totalCount;

  /**
   * Creates an empty enum count map with the specified key type.
   *
   * @param keyType the class object of the key type for this enum map
   * @throws NullPointerException if {@code keyType} is null
   */
  public EnumCount(Class<K> keyType) {
    _map = new EnumMap<>(keyType);
  }

  /**
   * Creates an enum count map with the given key type initially containing the same mappings (if
   * any).
   *
   * @param keyType   the class object of the key type for this enum count map
   * @param enumCount the enum map from which to initialize this enum map
   * @throws NullPointerException if one of the parameters is null
   */
  public EnumCount(Class<K> keyType, EnumCount<K> enumCount) {

    this(keyType);

    for (Map.Entry<K, MutableInteger> entry : enumCount._map.entrySet()) {
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
  public int increase(K key, int val) {

    MutableInteger entry = _map.get(key);

    if (entry == null) {

      _map.put(key, new MutableInteger(val));
      _totalCount++;

      return val;
    }

    _totalCount += val;
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
   * Decreases the count if possible and returns the old count value.
   *
   * @param key the key whose count should be decreased
   * @return the count after decreasing, or -1 if the count has not been decreased
   */
  public int decrease(K key) {

    MutableInteger entry = _map.get(key);

    if (entry == null) {
      return -1;
    }

    if (entry.get() == 0) {
      return -1;
    }

    _totalCount--;
    return entry.dec();
  }

  /** Returns the count for the given key. */
  public int count(K key) {

    MutableInteger entry = _map.get(key);

    if (entry == null) {
      return 0;
    }

    return entry.get();
  }

  public boolean contains(K key) {
    return count(key) > 0;
  }

  public int totalCount() {

    return _totalCount;
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof EnumCount)) {
      return false;
    }

    EnumCount other = (EnumCount) obj;
    return _totalCount == other._totalCount && Objects.equals(_map, other._map);
  }

  @Override public int hashCode() {
    return Objects.hashCode(_map);
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

    @Override public int hashCode() {
      return _val;
    }

    @Override public boolean equals(Object obj) {
      return obj instanceof MutableInteger && ((MutableInteger) obj)._val == _val;
    }

    @Override public String toString() {
      return Integer.toString(_val);
    }
  }

}
