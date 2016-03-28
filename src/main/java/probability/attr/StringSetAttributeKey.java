package probability.attr;


import com.opencsv.CSVParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StringSetAttributeKey extends AttributeKey<Set<String>> {

    private static final CSVParser parser = new CSVParser();

    public StringSetAttributeKey(String name, Set<String> defaultValue) {
        super(name, Set.class, defaultValue);
    }

    public StringSetAttributeKey(String name) {
        this(name, Collections.emptySet());
    }

    @Override
    public Set<String> parseValue(String valueString) throws AttributeParseException {

        List<String> list;
        try {
            list = Arrays.asList(parser.parseLine(valueString));
        } catch (IOException e) {
            throw new AttributeParseException(valueString
                    + " is not a parsable collection of comma separated values", this);
        }

        return list.stream().map(String::trim).collect(Collectors.toSet());
    }
}
