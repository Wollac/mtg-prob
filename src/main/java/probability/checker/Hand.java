package probability.checker;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import probability.core.Card;
import probability.core.Spell;
import probability.core.land.Land;

class Hand {

    private final int _startingHandSize;
    private final List<CardObject> _cards;
    private final Map<Integer, List<CardObject>> _landObjectCache = new HashMap<>();

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

        List<CardObject> landObjects = getCachedLandObjectsUntilTurn(turn);

        if (landObjects.isEmpty()) {
            return Collections.emptyList();
        }

        List<Land> result = new ArrayList<>(landObjects.size());
        for (CardObject cardObject : landObjects) {
            result.add((Land) cardObject.get());
        }

        return result;
    }

    public Set<CardObject> getUnplayedLandTypesUntilTurn(int turn) {

        List<CardObject> landObjects = getCachedLandObjectsUntilTurn(turn);

        if (landObjects.isEmpty()) {
            return Collections.emptySet();
        }

        Set<CardObject> result = new HashSet<>(landObjects.size());
        for (CardObject cardObject : landObjects) {

            if (!cardObject.isPlayed()) {
                result.add(cardObject);
            }
        }

        return result;
    }

    private Iterable<CardObject> getCardsUntilTurn(int turn) {
        return Iterables.limit(_cards, _startingHandSize + turn - 1);
    }

    private List<CardObject> computeLandObjectsUntilTurn(int turn) {

        List<CardObject> result = new ArrayList<>();
        for (CardObject cardObject : getCardsUntilTurn(turn)) {

            if (cardObject.get() instanceof Land) {
                result.add(cardObject);
            }
        }

        return result;
    }

    private List<CardObject> getCachedLandObjectsUntilTurn(int turn) {

        turn = Math.min(turn, _cards.size() - _startingHandSize + 1);

        return _landObjectCache.computeIfAbsent(turn, this::computeLandObjectsUntilTurn);
    }

    @Override
    public String toString() {
        return _cards.toString();
    }

}
