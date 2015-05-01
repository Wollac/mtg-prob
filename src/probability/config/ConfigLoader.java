package probability.config;

import probability.util.BooleanAttribute;
import probability.util.IntegerAttribute;

public class ConfigLoader extends AbstractConfigLoader implements Config {

	public ConfigLoader() {
		super();

		addAttribute(ATTR.NUMBER_OF_CARDS);
		addAttribute(ATTR.INITIAL_HAND_SIZE);
		addAttribute(ATTR.DRAW_ON_TURN);
	}

	@Override
	public int numberOfCards() {
		return getProperty(ATTR.NUMBER_OF_CARDS);
	}

	@Override
	public int initialHandSize() {
		return getProperty(ATTR.INITIAL_HAND_SIZE);
	}

	@Override
	public boolean drawOnTurn() {
		return getProperty(ATTR.DRAW_ON_TURN);
	}

	private interface ATTR {

		final static IntegerAttribute NUMBER_OF_CARDS = new IntegerAttribute(
				"cards", 60, i -> (i > 0));

		final static IntegerAttribute INITIAL_HAND_SIZE = new IntegerAttribute(
				"initial hand size", 7, i -> (i > 0));

		final static BooleanAttribute DRAW_ON_TURN = new BooleanAttribute(
				"draw on turn", false);

	}

}
