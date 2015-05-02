package probability.core;

public interface Card {

	public String getName();

	public CardType getCardType();

	public static enum CardType {
		Land, Spell, Other;
	}

}
