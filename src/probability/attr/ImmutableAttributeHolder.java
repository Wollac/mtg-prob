package probability.attr;

public interface ImmutableAttributeHolder {

	public <T> T getAttributeValue(AttributeKey<T> key, T def);

	public <T> T getAttributeValue(AttributeKey<T> key);

}
