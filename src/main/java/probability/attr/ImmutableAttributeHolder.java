package probability.attr;

public interface ImmutableAttributeHolder {

  /**
   * Returns the value of the corresponding {@link AttributeKey} or the provided default, if no
   * value has been assigned.
   *
   * @param key the key whose value should be returned
   * @param def the value which should be returned if the key has no assigned value
   */
  <T> T getAttributeValue(AttributeKey<T> key, T def);

  /**
   * Returns the value of the corresponding {@link AttributeKey} or the default associated with
   * that key, if no value has been assigned.
   */
  <T> T getAttributeValue(AttributeKey<T> key);

}
