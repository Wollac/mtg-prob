package probability.attr;

interface AttributeHolder extends ImmutableAttributeHolder {

    /**
     * Sets the value of the corresponding {@link AttributeKey}.
     *
     * @param key   the key whose value should be changed
     * @param value the new value of the corresponding type
     * @throws IllegalArgumentException if {@link AttributeKey#checkValid(Object)} throws an
     *                                  exception for the new {@code value}.
     */
    <T> void setAttributeValue(AttributeKey<T> key, T value);

    /**
     * Sets the value of the corresponding {@link AttributeKey}. The new value is not checked for
     * validity.
     *
     * @param key   the key whose value should be changed
     * @param value the new value of the corresponding type
     */
    <T> void setAttributeValueUnchecked(AttributeKey<T> key, T value);
}
