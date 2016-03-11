package probability.config;

import probability.attr.BooleanAttributeKey;
import probability.attr.IntegerAttributeKey;

public class ConfigLoader extends AbstractConfigLoader implements Config {

	public ConfigLoader() {
		super();

		addAttribute(ATTR.NUMBER_OF_CARDS);
		addAttribute(ATTR.INITIAL_HAND_SIZE);
		addAttribute(ATTR.DRAW_ON_TURN);
		addAttribute(ATTR.SAMPLE_SIZE);
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

	@Override
	public int sampleSize() {
		return getProperty(ATTR.SAMPLE_SIZE);
	}

	private interface ATTR {

		IntegerAttributeKey NUMBER_OF_CARDS = new IntegerAttributeKey(
				"cards", 60, i -> (i > 0));

		IntegerAttributeKey INITIAL_HAND_SIZE = new IntegerAttributeKey(
				"initial hand size", 7, i -> (i > 0));

		BooleanAttributeKey DRAW_ON_TURN = new BooleanAttributeKey(
				"draw on turn", false);

		IntegerAttributeKey SAMPLE_SIZE = new IntegerAttributeKey(
				"sample size", 10000, i -> (i >= 1000));

	}

}
