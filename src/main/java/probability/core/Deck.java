package probability.core;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import probability.config.Settings;
import probability.core.Card.CardType;
import probability.messages.ProjectException;

public class Deck {

    private final ArrayList<Card> _cards;

    public Deck() {
        _cards = new ArrayList<>();
    }

    private static String cardCountsToString(Multiset<Card> cardCounts,
                                             CardType type) {
        StringBuilder sb = new StringBuilder();

        if (type != null) {
            sb.append(type).append(" (").append(cardCounts.size()).append("):");
            sb.append(System.lineSeparator());
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
        if (_cards.size() > Settings.config.numberOfCards()) {
            throw new IllegalStateException(
                    "The predefined deck consists of more than "
                            + Settings.config.numberOfCards() + " cards");
        }

        for (int i = _cards.size(); i < Settings.config.numberOfCards(); i++) {
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
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    @Override
    public String toString() {

        final Multiset<Card> cardCounts = HashMultiset.create(_cards);

        StringBuilder sb = new StringBuilder();

        for (Card card : cardCounts.elementSet()) {
            sb.append(cardCounts.count(card)).append("x ").append(card);
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    private boolean containsNoSpell() {
        return _cards.stream().noneMatch(card -> card.getCardType() == CardType.Spell);
    }

    public void validate() throws ProjectException {

        if (_cards.size() > Settings.config.numberOfCards()) {
            throw new ProjectException((ProjectException.ProjectError.TOO_MANY_CARDS_IN_DECK));
        }

        if (containsNoSpell()) {
            throw new ProjectException(ProjectException.ProjectError.NO_SPELLS_IN_DECK);
        }
    }
}
