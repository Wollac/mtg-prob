package probability.config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import probability.attr.AttributeKey;
import probability.attr.AttributeHolder;
import probability.attr.AttributeKey.AttributeParseException;

public abstract class AbstractConfigLoader {

	private final Set<AttributeKey<?>> _attributes;

	private final AttributeHolder _attributeHolder;

	public AbstractConfigLoader(AttributeKey<?>... attributes) {

		_attributes = new HashSet<>(Arrays.asList(attributes));
		_attributeHolder = new AttributeHolder();

	}

	protected void addAttribute(AttributeKey<?> attribute) {
		_attributes.add(attribute);
	}

	public final void load(File configFile) throws IOException,
			ConfigParseException {

		try {
			JSONObject obj = new JSONObject(Files.toString(configFile,
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
			throw new ConfigParseException("error parsing file "
					+ configFile.getName(), e);
		}
	}

	public final void write(File configFile) {
		try {
			JSONObject obj = new JSONObject();

			for (AttributeKey<?> attribute : _attributes) {
				obj.put(attribute.getName(), attribute.getDefaultValue());
			}

			Files.write(obj.toString(2), configFile, Charsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public <T> T getProperty(AttributeKey<T> attribute) {
		return _attributeHolder.getAttributeValue(attribute);
	}

	public static class ConfigParseException extends Exception {

		private static final long serialVersionUID = 1L;

		public ConfigParseException(String str, Throwable cause) {
			super(str, cause);
		}

	}

}
