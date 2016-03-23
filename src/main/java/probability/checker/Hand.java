package probability.checker;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import probability.core.Card;
import probability.core.Spell;
import probability.core.land.Land;

class Hand {

    private final int _startingHandSize;

    private final List<CardObject> _cards;

    public Hand(Collection<CardObject> startingHand, Collection<CardObject> draws) {

        _startingHandSize = startingHand.size();

        _cards = new ArrayList<>(startingHand.size() + draws.size());
        _cards.addAll(startingHand);
        _cards.addAll(draws);
    }

    Hand(int startingHandSize, Collection<Card> draws) {

        Preconditions.checkArgument(draws.size() >= startingHandSize);

        _startingHandSize = startingHandSize;

        _cards = draws.stream().map(CardObject::new).collect(Collectors.toList());
    }

    public void markAllUnplayed() {

        _cards.stream().forEach(CardObject::markNotPlayed);
    }

    private Iterable<CardObject> getCardsUntilTurn(int turn) {
        return Iterables.limit(_cards, _startingHandSize + turn - 1);
    }

    public Set<Spell> getSpellTypesUntilTurn(int turn) {

        Set<Spell> result = new HashSet<>();
        for (Supplier<? extends Card> supplier : getCardsUntilTurn(turn)) {
            Card card = supplier.get();
            if (card instanceof Spell) {
                result.add((Spell) card);
            }
        }

        return result;
    }

    public List<Land> getLandCardsUntilTurn(int turn) {

        List<Land> result = new ArrayList<>();
        for (Supplier<? extends Card> supplier : getCardsUntilTurn(turn)) {
            Card card = supplier.get();
            if (card instanceof Land) {
                result.add((Land) card);
            }
        }

        return result;
    }

    public Set<CardObject> getUnplayedLandTypesUntilTurn(int turn) {

        Set<CardObject> result = new HashSet<>();
        for (CardObject cardObject : getCardsUntilTurn(turn)) {

            if (cardObject.isPlayed()) {
                continue;
            }

            Card card = cardObject.get();
            if (card instanceof Land) {
                result.add(cardObject);
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return _cards.toString();
    }

}
