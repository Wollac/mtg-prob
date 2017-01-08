package probability.attr;

import probability.messages.Displayable;
import probability.messages.Messages;

public class AttributeParseException extends Exception {

  public AttributeParseException(AttributeParseError error, AttributeKey<?> attribute) {
    super(getMessage(error, attribute));
  }

  public AttributeParseException(AttributeParseError error, AttributeKey<?> attribute,
      Throwable cause) {
    super(getMessage(error, attribute), cause);
  }

  private static String getMessage(AttributeParseError error, AttributeKey<?> attribute) {

    return attribute.getName() + ": " + Messages.getEnumText(error);
  }

  enum AttributeParseError implements Displayable {

    UNPARSABLE_VALUE, INVALID_VALUE
  }
}
