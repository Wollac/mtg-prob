package probability.core;

public final class CardUtils {
	private CardUtils() {
	}

	final static Card getDummyCard() {
		return DUMMY_CARD;
	}

	final static boolean isSpell(Card card) {
		return (card instanceof Spell);
	}

	final static boolean isLand(Card card) {
		return (card instanceof Land);
	}

	final static boolean isDummy(Card card) {
		return card.equals(DUMMY_CARD);
	}

	private static final Card DUMMY_CARD = new Card() {

		@Override
		public String getName() {
			return "Dummy";
		}

		@Override
		public String toString() {
			return getName();
		}
	};

}
