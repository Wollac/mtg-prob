package probability.config;

import probability.util.IntegerAttribute;

public class Config extends AbstractConfigLoader {

	public Config() {
		super();

		addAttribute(ATTR.NUMBER_OF_CARDS);
	}

	public int GetNumberOfCards() {
		return getProperty(ATTR.NUMBER_OF_CARDS);
	}

	private interface ATTR {

		final static IntegerAttribute NUMBER_OF_CARDS = new IntegerAttribute(
				"cards", 60, i -> (i > 0));
	}

}
