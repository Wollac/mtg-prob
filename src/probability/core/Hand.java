package probability.core;

import java.util.ArrayList;
import java.util.Collection;

import probability.core.land.Land;

public class Hand {

	private ArrayList<Collection<Card>> _cardsUntilTurn;

	private int _lastTurn;

	Hand(Collection<Card> startingHand, Collection<Card> draws) {

		_cardsUntilTurn = new ArrayList<>(draws.size() + 1);

		_cardsUntilTurn.add(startingHand);

		Collection<Card> last = startingHand;
		for (Card card : draws) {
			Collection<Card> cards = new ArrayList<>(last.size() + 1);
			cards.addAll(last);
			cards.add(card);

			_cardsUntilTurn.add(cards);
			last = cards;
		}

		_lastTurn = _cardsUntilTurn.size();
	}

	public Collection<Card> getCards() {
		return getCardsUntilTurn(_lastTurn);
	}

	public int size() {
		return getCards().size();
	}

	public Collection<Land> getLands() {
		return CardUtils.retainAllLandsToArrayList(getCards());
	}

	public Collection<Spell> getSpells() {
		return CardUtils.retainAllSpellsToArrayList(getCards());
	}

	/** Return all cards that are available up to the given turn */
	public Collection<Card> getCardsUntilTurn(int turn) {
		return _cardsUntilTurn.get(turn - 1);
	}

	public Collection<Land> getLandsUntilTurn(int turn) {
		Collection<Card> cards = getCardsUntilTurn(turn);

		return CardUtils.retainAllLandsToArrayList(cards);
	}

	public Collection<Spell> getSpellsUntilTurn(int turn) {
		Collection<Card> cards = getCardsUntilTurn(turn);

		return CardUtils.retainAllSpellsToArrayList(cards);
	}

	@Override
	public String toString() {
		return getCards().toString();
	}

}
