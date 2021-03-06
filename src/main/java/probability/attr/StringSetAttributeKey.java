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
    super(name, Set.class, defaultValue, s -> s != null);
  }

  public StringSetAttributeKey(String name) {
    this(name, Collections.emptySet());
  }

  @Override public Set<String> parseValue(String valueString) throws AttributeParseException {

    if (valueString.equals("[]")) {
      return Collections.emptySet();
    }

    if (valueString.startsWith("[") && valueString.endsWith("]")) {
      valueString = valueString.substring(1, valueString.length() - 1);
    }

    List<String> list;
    try {
      list = Arrays.asList(parser.parseLine(valueString));
    } catch (IOException e) {
      throw new AttributeParseException(
          AttributeParseException.AttributeParseError.UNPARSABLE_VALUE, this, e);
    }

    return list.stream().map(String::trim).collect(Collectors.toSet());
  }
}
