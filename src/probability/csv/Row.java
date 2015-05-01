package probability.csv;

import probability.attr.Attribute;

public interface Row {

	public <T> T getAttributeValue(Attribute<T> attribute, T defaultValue);

	public <T> T getAttributeVale(Attribute<T> attribute);

}
