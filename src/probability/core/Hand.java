package probability.core;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import probability.core.land.Land;

public class Hand {

    private final int _startingHandSize;

    private final List<Card> _cards;

    Hand(Collection<Card> startingHand, Collection<Card> draws) {

        _startingHandSize = startingHand.size();

        _cards = new ArrayList<>(startingHand.size() + draws.size());
        addAll(startingHand);
        addAll(draws);
    }

    private void addAll(Iterable<? extends Card> cards) {
        for (Card card : cards) {
            if (card instanceof Land)
                _cards.add(new Frame((Land) card));
            else
                _cards.add(card);
        }
    }

    public Collection<Card> getStartingHand() {

        List<Card> startingHand = new ArrayList<>(_startingHandSize);

        for (Card card : Iterables.limit(_cards, _startingHandSize)) {
            if (card instanceof Frame) {
                startingHand.add(((Frame) card).getCard());
            } else {
                startingHand.add(card);
            }
        }

        return startingHand;
    }

    public Set<Frame> getFramesUtilTurn(int turn) {

        Set<Frame> result = new HashSet<>();

        for (Card card : Iterables.limit(_cards, _startingHandSize + turn - 1)) {
            if (card instanceof Frame) {
                Frame frame = (Frame) card;

                if (!frame.isPlayed()) {
                    result.add(frame);
                }
            }
        }

        return result;
    }


    public Set<Spell> getSpellTypesUntilTurn(int turn) {
        Set<Spell> result = new HashSet<>();

        for (Card card : Iterables.limit(_cards, _startingHandSize + turn - 1)) {
            if (card instanceof Spell) {

                result.add((Spell) card);
            }
        }

        return result;
    }

    public List<Land> getLandsUntilTurn(int turn) {
        List<Land> result = new ArrayList<>();

        for (Card card : Iterables.limit(_cards, _startingHandSize + turn - 1)) {
            if (card instanceof Frame) {
                result.add(((Frame) card).getCard());
            }
        }

        return result;
    }


    public int size() {
        return _cards.size();
    }

    @Override
    public String toString() {
        return _cards.toString();
    }

    public static final class Frame implements Card {

        private final Land _land;

        private boolean _played;

        public Frame(Land land) {
            _land = land;
        }

        public void markPlayed() {
            _played = true;
        }

        public boolean isPlayed() {
            return _played;
        }

        public Land getCard() {
            return _land;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Frame && ((Frame) obj)._land.equals(_land);
        }

        @Override
        public int hashCode() {
            return _land.hashCode();
        }

        @Override
        public String getName() {
            return _land.getName();
        }

        @Override
        public CardType getCardType() {
            return _land.getCardType();
        }
    }

}
