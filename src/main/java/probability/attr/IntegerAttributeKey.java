package probability.attr;

import java.util.function.Predicate;

public class IntegerAttributeKey extends AttributeKey<Integer> {

  public IntegerAttributeKey(String name, int defaultValue, Predicate<Integer> validator) {
    super(name, Integer.class, defaultValue, validator);
  }

  public IntegerAttributeKey(String name, int defaultValue) {
    super(name, Integer.class, defaultValue);
  }

  public IntegerAttributeKey(String name) {
    this(name, 0);
  }

  @Override public Integer parseValue(String valueString) throws AttributeParseException {

    Integer result;

    try {
      result = Integer.valueOf(valueString);
    } catch (NumberFormatException e) {
      throw new AttributeParseException(
          AttributeParseException.AttributeParseError.UNPARSABLE_VALUE, this, e);
    }

    return result;
  }

}
