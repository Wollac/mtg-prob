package probability.attr;

import probability.core.ManaCost;

public class ManaCostAttributeKey extends AttributeKey<ManaCost> {

  public ManaCostAttributeKey(String name) {
    super(name, ManaCost.class, null);
  }

  @Override public ManaCost parseValue(String valueString) throws AttributeParseException {

    ManaCost result;

    try {
      result = new ManaCost(valueString);
    } catch (IllegalArgumentException e) {
      throw new AttributeParseException(
          AttributeParseException.AttributeParseError.UNPARSABLE_VALUE, this, e);
    }

    return result;
  }

}
