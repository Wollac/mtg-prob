package probability.csv;

public class IntegerAttribute extends Attribute<Integer> {

	public IntegerAttribute(String name, int defaultValue) {
		super(name, Integer.class, defaultValue);
	}

	public IntegerAttribute(String name) {
		super(name, Integer.class, 0);
	}

	@Override
	public Integer parseValue(String valueString) {
		return new Integer(valueString);
	}

}
