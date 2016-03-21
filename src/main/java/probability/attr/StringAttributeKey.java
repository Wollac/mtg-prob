package probability.attr;

import java.util.function.Predicate;

public class StringAttributeKey extends AttributeKey<String> {

    public StringAttributeKey(String name, String defaultValue,
                              Predicate<String> validator) {
        super(name, String.class, defaultValue, validator);
    }

    public StringAttributeKey(String name, String defaultValue) {
        super(name, String.class, defaultValue);
    }

    public StringAttributeKey(String name) {
        super(name, String.class, "");
    }

    @Override
    public String parseValue(String valueString) {
        return valueString;
    }

}
