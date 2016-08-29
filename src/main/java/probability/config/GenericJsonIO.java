package probability.config;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import probability.attr.AttributeHolder;
import probability.attr.AttributeKey;
import probability.attr.AttributeParseException;

class GenericJsonIO {

    private final Set<AttributeKey<?>> _attributes;

    private final AttributeHolder _attributeHolder;

    GenericJsonIO(Collection<? extends AttributeKey<?>> attributes) {

        _attributes = new HashSet<>(attributes);
        _attributeHolder = new AttributeHolder();
    }

    GenericJsonIO(AttributeKey<?>... attributes) {

        this(Arrays.asList(attributes));
    }

    protected void addAttribute(AttributeKey<?> attribute) {
        _attributes.add(attribute);
    }

    <T> T getProperty(AttributeKey<T> attribute) {
        return _attributeHolder.getAttributeValue(attribute);
    }

    public final void writeDefaultValues(Writer writer) {

        JSONObject obj = new JSONObject();

        for (AttributeKey<?> attribute : _attributes) {
            obj.put(attribute.getName(), attribute.getDefaultValue());
        }

        obj.write(writer);
    }

    public final void read(String string) throws JsonIOException {

        parse(new JSONObject(string));
    }

    public final void read(Reader reader) throws JsonIOException {

        parse(new JSONObject(new JSONTokener(reader)));
    }

    private void parse(JSONObject obj) throws JsonIOException {

        try {
            Set<String> propertyNames = obj.keySet();

            for (AttributeKey<?> attribute : _attributes) {
                String attributeName = attribute.getName();

                if (propertyNames.contains(attributeName)) {
                    // let our attributes do the parsing and not JSON
                    String objString = obj.get(attributeName).toString();
                    _attributeHolder.setParsedAttributeValue(attribute,
                            objString);
                }
            }
        } catch (JSONException | AttributeParseException e) {
            throw new JsonIOException(e);
        }
    }


    public static class JsonIOException extends Exception {

        public JsonIOException(Throwable cause) {
            super(cause);
        }

        public JsonIOException(String str, Throwable cause) {
            super(str, cause);
        }
    }

}
