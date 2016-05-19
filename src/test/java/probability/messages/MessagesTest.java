package probability.messages;

import org.junit.BeforeClass;
import org.junit.Test;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class tests all common problems regarding Messages. Missing translations, not used
 * translations, ENUM-translations. Everything is checked.
 */
public class MessagesTest {

    public static final Collection<Locale> LOCALES = Arrays.asList(Locale.ENGLISH, Locale.GERMAN);

    private static Collection<Method> messageMethods;

    private static Map<String, Properties> bundles;

    private static Collection<String> enumValueKeys;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void prepare() throws Exception {

        Reflections reflections = new Reflections("probability", new ResourcesScanner(), new SubTypesScanner());

        Collection<String> propertyFiles = reflections.getResources(Pattern.compile(Messages.BUNDLE_NAME + "_.*\\.properties"));

        bundles = new HashMap<>();
        for (String propertyFile : propertyFiles) {
            Properties properties = new Properties();
            URL url = ClassLoader.getSystemResource(propertyFile);
            properties.load(url.openStream());
            bundles.put(propertyFile, properties);
        }

        messageMethods = ReflectionUtils.getMethods(ProjectMessages.class);

        Collection<Class<? extends Displayable>> displayableEnums = reflections.getSubTypesOf(Displayable.class);

        enumValueKeys = new HashSet<>();
        for (Class displayableEnum : displayableEnums) {

            assertTrue(displayableEnum.isEnum());

            for (Object enumValue : displayableEnum.getEnumConstants()) {

                String messageKey = Messages.PREFIX_ENUM + displayableEnum.getSimpleName() + "_" + enumValue;
                enumValueKeys.add(messageKey);
            }
        }

    }

    /**
     * Is there an interface-method for every entry in our properties?
     *
     * @throws IOException ignore
     */
    @Test
    public void shouldHaveMessagesForAllInterfaceMethods() throws IOException {

        assertFalse("no message property files", bundles.isEmpty());

        Set<String> error = new HashSet<>();

        for (Method method : messageMethods) {

            for (Map.Entry<String, Properties> entry : bundles.entrySet()) {

                if (!entry.getValue().containsKey(method.getName())) {
                    error.add(entry.getKey() + "#" + method.getName());
                }
            }
        }

        if (!error.isEmpty()) {
            fail("No translations for " + error);
        }
    }

    @Test
    public void verifySignaturesOfAllInterfaceMethods() {

        messageMethods.forEach(this::verifySignatureForAllLocales);
    }

    private void verifySignatureForAllLocales(Method method) {

        assertEquals(String.class, method.getReturnType());

        Object[] arguments = getTestArguments(method);

        for (Locale locale : LOCALES) {

            Messages.setLocale(locale);

            try {
                method.invoke(Messages.get(), arguments);
            } catch (Exception ex) {
                throw new Error("Invoking message method threw exception", ex);
            }
        }
    }

    private Object[] getTestArguments(Method method) {

        List<Object> arguments = new ArrayList<>();

        for (Class<?> type : method.getParameterTypes()) {

            arguments.add(getTestValue(type));
        }

        return arguments.toArray();
    }

    private Object getTestValue(Class<?> type) {

        if (type.equals(int.class)) {
            return 0;
        }
        if (type.equals(String.class)) {
            return "";
        }
        if (type.equals(char.class)) {
            return 'a';
        }

        fail("Unexpected argument type " + type);

        return null;
    }

    /**
     * Is there an entry in each message.properties for every method in the interface?
     *
     * @throws IOException ignore
     */
    @Test
    public void shouldHaveInterfaceMethodForAllMessages() throws IOException {

        Set<String> methodNames = messageMethods.stream().map(Method::getName).collect(Collectors.toSet());

        Set<String> error = new HashSet<>();

        for (Map.Entry<String, Properties> entry : bundles.entrySet()) {

            for (Object messageObj : entry.getValue().keySet()) {
                String message = messageObj.toString();

                // Ignore ENUMs
                if (message.startsWith(Messages.PREFIX_ENUM)) {
                    continue;
                }

                if (!methodNames.contains(message)) {
                    error.add(entry.getKey() + "#" + message);
                }
            }
        }

        if (!error.isEmpty()) {
            fail("No interface method for : " + error);
        }
    }

    /**
     * Is there an entry in each message.properties for every Enum-Value?
     *
     * @throws Exception ignore
     */
    @Test
    public void shouldHaveEnumMessageForEveryEnumValue() throws Exception {

        List<String> missingKeys = new ArrayList<>();

        for (String enumValueKey : enumValueKeys) {

            for (Map.Entry<String, Properties> entry : bundles.entrySet()) {
                if (!entry.getValue().containsKey(enumValueKey)) {
                    missingKeys.add(entry.getKey() + "#" + enumValueKey);
                }
            }
        }

        if (!missingKeys.isEmpty()) {
            fail("No translation for " + missingKeys);
        }
    }

    /**
     * Is there an Enum-Value for every Enum-Translation in our message.properties?
     *
     * @throws IOException ignore
     */
    @Test
    public void shouldHaveEnumValueForEveryEnumMessage() throws IOException {

        Set<String> error = new HashSet<>();

        for (Map.Entry<String, Properties> entry : bundles.entrySet()) {

            for (Object messageObj : entry.getValue().keySet()) {
                String messageKey = messageObj.toString();
                if (messageKey.startsWith(Messages.PREFIX_ENUM) && !enumValueKeys.contains(messageKey)) {
                    error.add(entry.getKey() + "#" + messageKey);
                }
            }
        }

        if (!error.isEmpty()) {
            fail("No Enum value for " + error);
        }
    }

}