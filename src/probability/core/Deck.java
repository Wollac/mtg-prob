package probability.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

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

	public void add(Card card) {
		add(card, 1);
	}

	public void addAll(Collection<? extends Card> cards) {
		for (Card card : cards) {
			add(card);
		}
	}

	public List<Card> cards() {
		return Collections.unmodifiableList(_cards);
	}

	public void fillWithDummies(int total) {
		for (int i = _cards.size(); i < total; i++) {
			_cards.add(CardUtils.getDummyCard());
		}
	}

	public void shuffle() {
		java.util.Collections.shuffle(_cards);
	}

	public List<Card> draw(int n) {
		return Collections.unmodifiableList(_cards.subList(0, n));
	}

	@Override
	public String toString() {
		final ConcurrentMap<Card, AtomicInteger> map = new ConcurrentHashMap<>();

		for (Card card : _cards) {
			map.putIfAbsent(card, new AtomicInteger(0));
			map.get(card).incrementAndGet();
		}

		StringBuilder sb = new StringBuilder();

		for (Entry<Card, AtomicInteger> element : map.entrySet()) {
			sb.append(element.getValue() + "x " + element.getKey() + '\n');
		}

		return sb.toString();
	}

}
