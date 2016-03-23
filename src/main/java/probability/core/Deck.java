package probability.core;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import probability.config.Config;
import probability.core.Card.CardType;

public class Deck {

    private final Config _config;

    private final ArrayList<Card> _cards;

    public Deck(Config config) {
        _config = config;
        _cards = new ArrayList<>();
    }

    private static String cardCountsToString(Multiset<Card> cardCounts,
                                             CardType type) {
        StringBuilder sb = new StringBuilder();

        if (type != null) {
            sb.append(type).append(" (").append(cardCounts.size()).append("):\n");
        }

        for (Card card : CardUtils.sortCardsByName(cardCounts.elementSet())) {
            sb.append(String.format("%2dx " + card + "%n",
                    cardCounts.count(card), card));
        }

        return sb.toString();
    }

    private void add(Card card, int num) {
        for (int i = 0; i < num; i++) {
            _cards.add(card);
        }
    }

    private void add(Card card) {
        add(card, 1);
    }

    public void addAll(Collection<? extends Card> cards) {
        cards.forEach(this::add);
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

    public boolean isEmpty() {

        return _cards.isEmpty();
    }

    public String toFormattedString() {
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

    @Override
    public String toString() {

        final Multiset<Card> cardCounts = HashMultiset.create(_cards);

        StringBuilder sb = new StringBuilder();

        for (Card card : cardCounts.elementSet()) {
            sb.append(cardCounts.count(card)).append("x ").append(card).append('\n');
        }

        return sb.toString();
    }

}
