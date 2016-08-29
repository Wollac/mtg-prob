package probability.csv;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import probability.attr.AttributeHolder;
import probability.attr.AttributeKey;
import probability.attr.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;
import probability.utils.LineCommentReader;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractCSVParser<T> {

    private final CSVReader _reader;

    private final char _separator;

    private final Map<AttributeKey<?>, Boolean> _attributes;

    private Map<AttributeKey<?>, Integer> _attribute2column;

    AbstractCSVParser(Reader reader, char separator) throws IOException {

        checkNotNull(reader);

        _reader = new CSVReader(new LineCommentReader(reader), separator);
        _separator = separator;

        // the attributes should be ordered as added
        _attributes = new LinkedHashMap<>();
    }

    AbstractCSVParser(Reader reader) throws IOException {
        this(reader, CSVParser.DEFAULT_SEPARATOR);
    }

    protected void addAttribute(AttributeKey<?> attribute, boolean mandatory) {

        checkNotNull(attribute);
        checkArgument(!_attributes.containsKey(attribute), "attribute already added");
        checkArgument(!attribute.getName().startsWith(" "),
                " attribute name must not start with a space");
        checkArgument(!attribute.getName().endsWith(" "),
                " attribute name must not end with a space");
        checkArgument(!headersContain(attribute.getName()),
                "attribute with same name already added");

        _attributes.put(attribute, mandatory);
    }

    protected void addOptionalAttribute(AttributeKey<?> attribute) {
        addAttribute(attribute, false);
    }

    protected void addMandatoryAttribute(AttributeKey<?> attribute) {
        addAttribute(attribute, true);
    }

    private boolean headersContain(String name) {
        return _attributes.keySet().stream().anyMatch(k -> k.getName().equalsIgnoreCase(name));
    }

    public List<T> readAll() throws IOException, CvsParseException {

        if (_attributes.isEmpty()) {
            throw new IllegalStateException("At least one attribute is needed.");
        }

        readHeader();

        List<T> instances = new ArrayList<>();

        String[] nextLine;
        while ((nextLine = _reader.readNext()) != null) {
            ImmutableAttributeHolder row = parseLine(nextLine);

            instances.addAll(createInstance(row));
        }

        return instances;
    }

    public List<String> getHeaders() {
        return _attributes.keySet().stream().map(AttributeKey::getName).collect(Collectors.toList());
    }

    public char getSeparator() {
        return _separator;
    }

    protected abstract Collection<T> createInstance(ImmutableAttributeHolder row);

    private ImmutableAttributeHolder parseLine(String[] nextLine)
            throws CvsParseException {

        AttributeHolder attributeHolder = new AttributeHolder();

        try {
            for (Entry<AttributeKey<?>, Integer> element : _attribute2column
                    .entrySet()) {
                String valueString = nextLine[element.getValue()].trim();

                attributeHolder.setParsedAttributeValue(element.getKey(),
                        valueString);
            }
        } catch (AttributeParseException e) {
            throw new CvsParseException("error parsing csv file", e);
        }

        return attributeHolder;
    }

    private void readHeader() throws IOException, CvsParseException {

        _attribute2column = new HashMap<>();

        List<String> line = Arrays.asList(_reader.readNext());

        for (AttributeKey<?> attribute : _attributes.keySet()) {

            int index = indexOf(line, attribute.getName());

            if (index >= 0) {
                _attribute2column.put(attribute, index);
            } else if (isMandatory(attribute)) {

                throw new CvsParseException("error parsing csv file",
                        new Throwable("Mandatory column \"" + attribute.getName()
                                + "\" not found"));
            }
        }
    }

    private int indexOf(List<String> row, String string) {

        int index = 0;
        for (String cell : row) {
            if (cell.trim().equalsIgnoreCase(string)) {
                return index;
            }
            index++;
        }

        return -1;
    }

    private boolean isMandatory(AttributeKey<?> attribute) {
        return _attributes.get(attribute);
    }

    public static class CvsParseException extends Exception {

        CvsParseException(String str, Throwable cause) {
            super(str, cause);
        }

    }

}
