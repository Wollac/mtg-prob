package probability.config;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import probability.attr.AttributeHolder;
import probability.attr.AttributeKey;
import probability.attr.AttributeKey.AttributeParseException;

class GenericJsonIO {

    private final Set<AttributeKey<?>> _attributes;

    private final AttributeHolder _attributeHolder;

    GenericJsonIO(AttributeKey<?>... attributes) {

        _attributes = new HashSet<>(Arrays.asList(attributes));
        _attributeHolder = new AttributeHolder();

    }

    protected void addAttribute(AttributeKey<?> attribute) {
        _attributes.add(attribute);
    }

    <T> T getProperty(AttributeKey<T> attribute) {
        return _attributeHolder.getAttributeValue(attribute);
    }

    public final void load(File file) throws IOException,
            JsonIOException {

        try {
            JSONObject obj = new JSONObject(Files.toString(file,
                    Charsets.UTF_8));

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
            throw new JsonIOException("error parsing file "
                    + file.getName(), e);
        }
    }

    public final void writeDefaultValues(File file) throws IOException {

        JSONObject obj = new JSONObject();

        for (AttributeKey<?> attribute : _attributes) {
            obj.put(attribute.getName(), attribute.getDefaultValue());
        }

        Files.write(obj.toString(2), file, Charsets.UTF_8);
    }


    public static class JsonIOException extends Exception {

        private static final long serialVersionUID = 1L;

        public JsonIOException(String str, Throwable cause) {
            super(str, cause);
        }

    }

}
