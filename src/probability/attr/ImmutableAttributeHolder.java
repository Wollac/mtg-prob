package probability.attr;

public interface ImmutableAttributeHolder {

	public <T> T getAttributeValue(Attribute<T> key, T def);

	public <T> T getAttributeVale(Attribute<T> key);

}
