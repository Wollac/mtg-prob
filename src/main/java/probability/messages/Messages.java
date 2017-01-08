package probability.messages;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Entry-Point for Messages. Provides access to ResourceBundles.
 *
 * <p>Messages can be accessed by calling {@link Messages#get()}.nameOfMessage().
 * This class is using a Java Proxy class to resolve the probability.messages.
 *
 * <p>Enum-Translations can be accessed by calling {@link Messages#getEnumText(Displayable)}.
 * The enum has to implement {@link Displayable} and there has to be an entry in the
 * <i>message.properties</i> with the following pattern:
 * "Enum_" + Enum-Short-Classname + "_" + Enum-Value
 */
public final class Messages {

  static final String PREFIX_ENUM = "Enum_";

  static final String BUNDLE_NAME = "messages";
  private static final ProjectMessages messages = (ProjectMessages) Proxy.newProxyInstance(//
      ProjectMessages.class.getClassLoader(),//
      new Class[] {ProjectMessages.class}, //
      new MessageResolver());
  private static Locale _locale = Locale.getDefault();

  private Messages() {
    // No instances
  }

  public static void setLocale(Locale locale) {

    _locale = Objects.requireNonNull(locale);
  }

  /**
   * Returns the localized text of the Enumeration.
   *
   * @param enumValue Value of {@link Displayable}-Enumeration
   */
  public static String getEnumText(Displayable enumValue) {
    String key = PREFIX_ENUM + enumValue.getClass().getSimpleName() + "_" + enumValue.toString();
    return getString(key, null);
  }

  /**
   * @return Proxy of ProjectMessages - Can be used to access all probability.messages
   */
  public static ProjectMessages get() {
    return messages;
  }

  private static String getString(String key, Object[] args) {

    try {
      String message =
          ResourceBundle.getBundle("probability/" + BUNDLE_NAME, _locale).getString(key);
      if (args != null) {
        MessageFormat formatter = new MessageFormat(message, _locale);
        message = formatter.format(args);
      }
      return message;
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }

  private static class MessageResolver implements InvocationHandler {
    @Override public Object invoke(Object proxy, Method method, Object[] args) {

      String methodName = method.getName();

      return Messages.getString(methodName, args);
    }
  }
}
