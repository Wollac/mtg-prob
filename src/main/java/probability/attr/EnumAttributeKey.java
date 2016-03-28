package probability.attr;

import java.util.Arrays;
import java.util.function.Predicate;

public class EnumAttributeKey<E extends Enum<E>> extends AttributeKey<E> {

    public EnumAttributeKey(String name, Class<E> type, E defaultValue,
                            Predicate<E> validator) {
        super(name, type, defaultValue, validator);
    }

    public EnumAttributeKey(String name, Class<E> type, E defaultValue) {
        this(name, type, defaultValue, e -> true);
    }

    @Override
    public E parseValue(String valueString) throws AttributeParseException {
        E result;

        // this will only be called, when the type was set to Class<E>
        @SuppressWarnings("unchecked")
        Class<E> enumType = (Class<E>) getValueType();

        try {
            result = Enum.valueOf(enumType, valueString);
        } catch (IllegalArgumentException e) {
            throw new AttributeParseException("illegal string \"" + valueString
                    + "\" is not a valid element of "
                    + Arrays.asList(getValueType().getEnumConstants()), this);
        }

        return result;
    }
}
