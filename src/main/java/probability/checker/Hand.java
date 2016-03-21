package probability.checker;

import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import probability.core.Card;
import probability.core.Spell;
import probability.core.land.Land;

public class Hand {

    private final int _startingHandSize;

    private final List<Card> _cards;

    public Hand(Collection<Card> startingHand, Collection<Card> draws) {

        _startingHandSize = startingHand.size();

        _cards = new ArrayList<>(startingHand.size() + draws.size());
        addAll(startingHand);
        addAll(draws);
    }

    private void addAll(Iterable<? extends Card> cards) {
        for (Card card : cards) {
            if (card instanceof Land)
                _cards.add(new PlayableLand((Land) card));
            else
                _cards.add(card);
        }
    }

    public List<Card> getStartingHand() {

        List<Card> startingHand = new ArrayList<>(_startingHandSize);

        for (Card card : Iterables.limit(_cards, _startingHandSize)) {
            if (card instanceof PlayableLand) {
                startingHand.add(((PlayableLand) card).getLand());
            } else {
                startingHand.add(card);
            }
        }

        return startingHand;
    }

    public Set<PlayableLand> getPlayableLandTypesUtilTurn(int turn) {

        Set<PlayableLand> result = new HashSet<>();

        for (Card card : Iterables.limit(_cards, _startingHandSize + turn - 1)) {
            if (card instanceof PlayableLand) {
                PlayableLand frame = (PlayableLand) card;

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
            if (card instanceof PlayableLand) {
                result.add(((PlayableLand) card).getLand());
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

}
