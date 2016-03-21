package probability.core;

public interface Card {

    String getName();

    CardType getCardType();

    enum CardType {
        Land, Spell, Other
    }

}
