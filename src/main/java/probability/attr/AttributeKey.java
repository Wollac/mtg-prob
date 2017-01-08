package probability.attr;

import java.util.function.Predicate;

public abstract class AttributeKey<T> {

  private final String _name;

  private final T _defaultValue;

  private final Predicate<T> _validator;

  private final Class<? super T> _type;

  AttributeKey(String name, Class<? super T> type, T defaultValue, Predicate<T> validator) {
    _name = name;
    _defaultValue = defaultValue;
    _type = type;
    _validator = validator;
  }

  AttributeKey(String name, Class<? super T> type, T defaultValue) {

    // initialize with tautology
    this(name, type, defaultValue, t -> true);
  }

  public final String getName() {
    return _name;
  }

  public final T getDefaultValue() {
    return _defaultValue;
  }

  public final Class<? super T> getValueType() {
    return _type;
  }

  /** Returns whether the key is valid wrt. the provided validator. */
  public final boolean isValid(T value) {
    return _validator.test(value);
  }

  /**
   * Checks whether the key is valid wrt. the provided validator.
   *
   * @throws AttributeParseException if the value is not valid.
   */
  public final void checkValid(T value) throws AttributeParseException {

    if (!isValid(value)) {

      throw new AttributeParseException(AttributeParseException.AttributeParseError.INVALID_VALUE,
          this);
    }
  }

  public abstract T parseValue(String valueString) throws AttributeParseException;

  @Override public String toString() {
    return getName() + "[" + getValueType().getSimpleName() + "]";
  }

}
