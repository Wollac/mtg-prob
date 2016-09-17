package probability.attr;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Represents an {@link AttributeHolder} that is backed by {@link Properties}.
 * The {@code Properties} can be saved to a stream or loaded from a stream.
 *
 * @see java.util.Properties
 */
public class PropertiesAttributeHolder implements AttributeHolder {

    private final Set<AttributeKey<?>> _attributes;

    private final ParsingAttributeHolder _attributeHolder;

    public PropertiesAttributeHolder(Collection<? extends AttributeKey<?>> attributes) {

        _attributes = new HashSet<>(attributes);
        _attributeHolder = new ParsingAttributeHolder();
    }

    public PropertiesAttributeHolder(AttributeKey<?>... attributes) {
        this(Arrays.asList(attributes));
    }

    @Override
    public <T> void setAttributeValue(AttributeKey<T> key, T value) {
        _attributeHolder.setAttributeValue(key, value);
    }

    @Override
    public <T> void setAttributeValueUnchecked(AttributeKey<T> key, T value) {
        _attributeHolder.setAttributeValueUnchecked(key, value);
    }

    @Override
    public <T> T getAttributeValue(AttributeKey<T> key, T def) {
        return _attributeHolder.getAttributeValue(key, def);
    }

    @Override
    public <T> T getAttributeValue(AttributeKey<T> key) {
        return _attributeHolder.getAttributeValue(key);
    }

    protected void addAttribute(AttributeKey<?> attribute) {
        _attributes.add(attribute);
    }

    /**
     * Reads the attributes from the input character stream using {@link Properties#load(Reader)}.
     * <p>
     * For each {@code AttributeKey} the value string of the key corresponding to its name is then
     * parsed using {@link ParsingAttributeHolder#setParsedAttributeValue(AttributeKey, String)}.
     * <p>
     * The specified stream remains open after this method returns.
     *
     * @param reader the input character stream.
     * @throws IOException             if an error occurred when reading from the input stream.
     * @throws AttributeParseException if one of the attributes could not be parsed using the
     *                                 properties value string.
     */
    public void load(Reader reader) throws IOException, AttributeParseException {

        Properties properties = new Properties();
        properties.load(reader);

        for (AttributeKey<?> key : _attributes) {
            String valueString = properties.getProperty(key.getName());
            if (valueString != null) {
                _attributeHolder.setParsedAttributeValue(key, valueString);
            }
        }
    }

    /**
     * Writes the attributes (key and value pairs) to the output character stream using
     * {@link Properties#store(Writer, String)}.
     * <p>
     * After the entries have been written, the output stream is flushed.
     * The output stream remains open after this method returns.
     *
     * @param writer      an output character stream writer.
     * @param description a description of the property list.
     * @throws IOException if writing this property list to the specified output stream throws an
     *                     {@code IOException}.
     */
    public void store(Writer writer, String description) throws IOException {

        Properties properties = new Properties();

        for (AttributeKey<?> key : _attributes) {
            properties.setProperty(key.getName(),
                    _attributeHolder.getAttributeValue(key).toString());
        }

        properties.store(writer, description);
    }
}
