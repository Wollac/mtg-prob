package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	ArrayList<Card> _cards;

	public Deck() {
		_cards = new ArrayList<>();
	}

	public void add(Card card, int num) {
		for (int i = 0; i < num; i++) {
			_cards.add(card);
		}
	}

	public List<Card> cards() {
		return Collections.unmodifiableList(_cards);
	}

	public void fillWithDummies(int total) {
		for (int i = _cards.size(); i < total; i++) {
			_cards.add(new DummyCard());
		}
	}

	public void shuffle() {
		java.util.Collections.shuffle(_cards);
	}

	public List<Card> draw(int n) {
		return Collections.unmodifiableList(_cards.subList(0, n));
	}


}
