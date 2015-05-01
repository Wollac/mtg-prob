package probability.config;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import probability.util.Attribute;
import probability.util.AttributeHolder;

abstract class AbstractConfigLoader {

	private final Set<Attribute<?>> _attributes;

	private final AttributeHolder _attributeHolder;

	public AbstractConfigLoader(Attribute<?>... attributes) {

		_attributes = new HashSet<>(Arrays.asList(attributes));
		_attributeHolder = new AttributeHolder();

	}

	protected void addAttribute(Attribute<?> attribute) {
		_attributes.add(attribute);
	}

	public final void load(Reader reader) throws IOException {
		Properties properties = new Properties();
		properties.load(reader);

		Set<String> propertyNames = properties.stringPropertyNames();

		for (Attribute<?> attribute : _attributes) {
			String attributeName = attribute.getName();

			if (propertyNames.contains(attributeName)) {
				_attributeHolder.setParsedAttributeValue(attribute,
						properties.getProperty(attributeName));
			}
		}
	}

	public <T> T getProperty(Attribute<T> attribute) {
		return _attributeHolder.getAttributeVale(attribute);
	}

}
