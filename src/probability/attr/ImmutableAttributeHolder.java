package probability.attr;

public interface ImmutableAttributeHolder {

	<T> T getAttributeValue(AttributeKey<T> key, T def);

	<T> T getAttributeValue(AttributeKey<T> key);

}
