package probability.core;

import java.util.ArrayList;
import java.util.Collection;

import probability.core.land.Land;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class Hand {

	private ListMultimap<Integer, Card> _cardsByTurn;

	private int _lastTurn;

	Hand(Collection<Card> startingHand, Collection<Card> draws) {

		_cardsByTurn = ArrayListMultimap.create();

		for (Card card : startingHand) {
			_cardsByTurn.put(1, card);
		}

		int turn = 2;
		for (Card card : draws) {
			_cardsByTurn.put(turn++, card);
		}

		_lastTurn = turn - 1;
	}

	public Collection<Card> getCards() {
		return _cardsByTurn.values();
	}

	public Collection<Land> getLands() {
		return CardUtils.retainAllLandsToArrayList(getCards());
	}

	public Collection<Spell> getSpells() {
		return CardUtils.retainAllSpellsToArrayList(getCards());
	}

	/** Return all cards that are available up to the given turn */
	public Collection<Card> getCardsUntilTurn(int turn) {
		Collection<Card> cards = new ArrayList<>();

		int last = Math.min(turn, _lastTurn);

		for (int i = 0; i <= last; i++) {
			cards.addAll(_cardsByTurn.get(i));
		}

		return cards;
	}

	public Collection<Land> getLandsUntilTurn(int turn) {
		Collection<Card> cards = getCardsUntilTurn(turn);

		return CardUtils.retainAllLandsToArrayList(cards);
	}

	public Collection<Spell> getSpellsUntilTurn(int turn) {
		Collection<Card> cards = getCardsUntilTurn(turn);

		return CardUtils.retainAllSpellsToArrayList(cards);
	}

	public Collection<Card> getCardsInTurn(int turn) {
		return _cardsByTurn.get(turn);
	}

	public Collection<Land> getLandsInTurn(int turn) {
		return CardUtils.retainAllLandsToArrayList(getCardsInTurn(turn));
	}

	public Collection<Spell> getSpellsInTurn(int turn) {
		return CardUtils.retainAllSpellsToArrayList(getCardsInTurn(turn));
	}
	
	@Override
	public String toString() {
		return getCards().toString();
	}

}
