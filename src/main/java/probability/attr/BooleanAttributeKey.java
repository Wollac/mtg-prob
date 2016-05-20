package probability.attr;

import java.util.function.Predicate;

public class BooleanAttributeKey extends AttributeKey<Boolean> {

    public BooleanAttributeKey(String name, boolean defaultValue,
                               Predicate<Boolean> validator) {
        super(name, Boolean.class, defaultValue, validator);
    }

    public BooleanAttributeKey(String name, boolean defaultValue) {
        super(name, Boolean.class, defaultValue);
    }

    public BooleanAttributeKey(String name) {
        super(name, Boolean.class, false);
    }

    @Override
    public Boolean parseValue(String valueString)
            throws AttributeParseException {

        if (valueString.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (valueString.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }

        throw new AttributeParseException(AttributeParseException.AttributeParseError.UNPARSABLE_VALUE, this);
    }

}
