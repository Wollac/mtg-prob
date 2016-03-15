package probability.csv;

import com.opencsv.CSVReader;
import probability.attr.AttributeHolder;
import probability.attr.AttributeKey;
import probability.attr.AttributeKey.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.Map.Entry;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractCSVParser<T> {

    private final CSVReader _reader;

    private final Map<AttributeKey<?>, Boolean> _attributes;

    private Map<AttributeKey<?>, Integer> _attribute2column;

    protected AbstractCSVParser(Reader reader) throws IOException {

        checkNotNull(reader);

        _reader = new CSVReader(new LineCommentReader(reader));
        _attributes = new HashMap<>();
    }

    protected void addAttribute(AttributeKey<?> attribute, boolean mandatory) {

        checkNotNull(attribute);
        checkArgument(!_attributes.containsKey(attribute), "attribute already added");
        checkArgument(attribute.getName().startsWith(" "),
                " attribute name must not start with a space");
        checkArgument(attribute.getName().endsWith(" "),
                " attribute name must not end with a space");

        _attributes.put(attribute, mandatory);
    }

    protected void addOptionalAttribute(AttributeKey<?> attribute) {
        addAttribute(attribute, false);
    }

    protected void addMandatoryAttribute(AttributeKey<?> attribute) {
        addAttribute(attribute, true);
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

        private static final long serialVersionUID = 1L;

        public CvsParseException(String str, Throwable cause) {
            super(str, cause);
        }

    }

}
