package probability.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import probability.config.Config;
import probability.core.Card.CardType;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class Deck {

	private final Config _config;

	ArrayList<Card> _cards;

	public Deck(Config config) {
		_config = config;
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

	public void fillWithDummies() {
		if (_cards.size() > _config.numberOfCards()) {
			throw new IllegalStateException(
					"The predefined deck consists of more than "
							+ _config.numberOfCards() + " cards");
		}

		for (int i = _cards.size(); i < _config.numberOfCards(); i++) {
			_cards.add(CardUtils.getDummyCard());
		}
	}

	public void shuffle() {
		java.util.Collections.shuffle(_cards);
	}

	public Hand draw(int turn) {

		int handSize = _config.initialHandSize();

		if (_config.drawOnTurn()) {
			handSize++;
		}

		Collection<Card> startingHand = Collections.unmodifiableList(_cards
				.subList(0, handSize));

		int totalSize = handSize + turn - 1;

		Collection<Card> drawnCards = Collections.unmodifiableList(_cards
				.subList(handSize, totalSize));

		return new Hand(startingHand, drawnCards);
	}

	public String toFormatedString() {
		Map<CardType, Multiset<Card>> cardCounts = new HashMap<>();

		for (CardType type : CardType.values()) {
			cardCounts.put(type, HashMultiset.create());
		}

		for (Card card : _cards) {
			cardCounts.get(card.getCardType()).add(card);
		}

		StringBuilder sb = new StringBuilder();

		for (CardType type : CardType.values()) {
			sb.append(cardCountsToString(cardCounts.get(type), type));
			sb.append('\n');
		}

		return sb.toString();
	}

	private static String cardCountsToString(Multiset<Card> cardCounts,
			CardType type) {
		StringBuilder sb = new StringBuilder();

		if (type != null) {
			sb.append(type + " (" + cardCounts.size() + "):\n");
		}

		for (Card card : CardUtils.sortCardsByName(cardCounts.elementSet())) {
			sb.append(String.format("%2dx " + card + "%n",
					cardCounts.count(card), card));
		}

		return sb.toString();
	}

	@Override
	public String toString() {
		final Multiset<Card> cardCounts = HashMultiset.create();

		for (Card card : _cards) {
			cardCounts.add(card);
		}

		StringBuilder sb = new StringBuilder();

		for (Card card : cardCounts.elementSet()) {
			sb.append(cardCounts.count(card) + "x " + card + '\n');
		}

		return sb.toString();
	}

}
