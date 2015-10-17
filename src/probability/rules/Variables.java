package probability.rules;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import probability.attr.AttributeHolder;
import probability.attr.AttributeKey;
import probability.attr.ImmutableAttributeHolder;

public class Variables {

  private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]\\w*");

  private static final Map<String, Variable<?>> _name2var = new HashMap<>();

  private static final AttributeHolder _bindings = new AttributeHolder();

  public static <T> void registerVariable(AttributeKey<T> key) {
    registerVariable(key, key.getName());
  }

  public static <T> void registerVariable(AttributeKey<T> key, String name) {

    checkName(name);

    _name2var.put(name, Variable.createVariable(key));
  }

  public static <T> void registerAndAssign(AttributeKey<T> key, T value) {
    registerVariable(key);
    assignValue(key, value);
  }

  public static boolean isRegistered(AttributeKey<?> key) {

    for (Variable<?> var : _name2var.values()) {
      if (var.getAttributeKey().equals(key))
        return true;
    }
    return false;
  }

  public static boolean isRegistered(String name) {
    return _name2var.containsKey(name);
  }

  public static <T> void assignValue(AttributeKey<T> key, T value) {

    if (!isRegistered(key)) {
      throw new IllegalArgumentException("No variable has been registered for this key");
    }

    _bindings.setAttributeValue(key, value);
  }

  static ImmutableAttributeHolder getBindings() {
    return _bindings;
  }

  static Variable<?> getVariable(String name) {
    return _name2var.get(name);
  }

  private static void checkName(String name) {

    if (!NAME_PATTERN.matcher(name).matches()) {
      throw new IllegalArgumentException(
          "Invalid variable name: All names must match " + NAME_PATTERN);
    }

    if (_name2var.containsKey(name)) {
      throw new IllegalArgumentException(
          "Invalid variable name: " + name + " has already been registered");
    }

    if (Operations.getOperation(name) != null) {
      throw new IllegalArgumentException(
          "Invalid variable name: " + name + " is the name of an operation");
    }
  }

}
