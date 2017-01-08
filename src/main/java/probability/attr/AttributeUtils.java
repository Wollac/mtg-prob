package probability.attr;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class AttributeUtils {

  private AttributeUtils() {
  }

  /**
   * Returns the values of all static final fields that extend AttributeKey.
   *
   * @throws IllegalArgumentException if the class does not contain any fields or contains a field
   *                                  of different type
   */
  public static List<AttributeKey<?>> getAttributeKeys(Class clazz) {

    if (clazz.getFields().length == 0) {
      throw new IllegalArgumentException("error in " + clazz + ": no fields contained");
    }

    List<AttributeKey<?>> result = new ArrayList<>();

    for (Field field : clazz.getFields()) {

      if (!isValidAttributeKey(field)) {
        throw new IllegalArgumentException("error in " + clazz + ": field " + field.getName()
            + " is not a static final AttributeKey");
      }

      field.setAccessible(true);
      try {
        result.add((AttributeKey<?>) field.get(null));
      } catch (IllegalAccessException e) {
        throw new IllegalArgumentException("error in " + clazz, e);
      }

    }

    return result;
  }

  private static boolean isValidAttributeKey(Field field) {

    final int mod = field.getModifiers();

    return Modifier.isStatic(mod) && Modifier.isFinal(mod) && AttributeKey.class
        .isAssignableFrom(field.getType());
  }
}
