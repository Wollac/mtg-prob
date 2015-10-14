package probability.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Hand {

	private final int _startingHandSize;

	private final List<Card> _cards;

	Hand(Collection<Card> startingHand, Collection<Card> draws) {

		_startingHandSize = startingHand.size();

		_cards = new ArrayList<Card>(startingHand.size() + draws.size());
		_cards.addAll(startingHand);
		_cards.addAll(draws);
	}

	public Collection<Card> getStartingHand() {
		return Collections.unmodifiableCollection(_cards.subList(0,
				_startingHandSize));
	}

	public Collection<Card> getDraws() {
		return Collections.unmodifiableCollection(_cards.subList(
				_startingHandSize, _cards.size()));
	}

	public Collection<Card> getCardsInTurn(int turn) {
		if (turn == 1) {
			return getStartingHand();
		}

		return Collections.singleton(_cards.get(_startingHandSize + turn - 2));
	}

	public Collection<Card> getCardsUntilTurn(int turn) {
		return Collections.unmodifiableCollection(_cards.subList(0,
				_startingHandSize + turn - 1));
	}

	public int getNumerOfLandsInStartingHand() {
		int count = 0;

		for (Card card : _cards.subList(0, _startingHandSize)) {
			if (CardUtils.isLand(card)) {
				count++;
			}
		}

		return count;
	}

	public int getStartingHandSize() {
		
		return _startingHandSize;
	}

	public int size() {
		return _cards.size();
	}

	public int getLastTurn() {
		return _cards.size() - _startingHandSize + 1;
	}

	@Override
	public String toString() {
		return _cards.toString();
	}

}
