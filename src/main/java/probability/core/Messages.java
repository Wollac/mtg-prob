package probability.core;


import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Messages {

    private static final String BASE_NAME = "messages";

    private static final ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME);

    private Messages() {
        // do not initialize
    }

    public static String formatMessage(MessageKey messageKey,
                                       Object... arguments) {

        String pattern;
        try {
            pattern = bundle.getString(messageKey.getBundleKey());
        } catch (MissingResourceException e) {
            return messageKey.toString();
        }

        return MessageFormat.format(pattern, arguments);
    }

    public interface MessageKey {

        String getBundleKey();

    }
}
