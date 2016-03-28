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

import probability.core.Card;
import probability.core.CardUtils;
import probability.core.Spell;
import probability.core.land.Land;

class Hand {

    private final int _startingHandSize;
    private final List<IdentifiedCardObject> _cards;
    private final Map<Integer, List<IdentifiedCardObject>> _landObjectCache = new HashMap<>();

    public Hand(Collection<IdentifiedCardObject> startingHand, Collection<IdentifiedCardObject> draws) {

        _startingHandSize = startingHand.size();

        _cards = new ArrayList<>(startingHand.size() + draws.size());
        _cards.addAll(startingHand);
        _cards.addAll(draws);
    }

    Hand(int startingHandSize, Collection<? extends Card> cards) {

        Preconditions.checkArgument(cards.size() >= startingHandSize);

        _startingHandSize = startingHandSize;
        _cards = IdentifiedCardObject.toCardObjects(cards);
    }

    public void markAllInHand() {

        _cards.stream().forEach(IdentifiedCardObject::markNotPlayed);
    }

    public Set<Spell> getSpellTypesUntilTurn(int turn) {

        Set<Spell> result = new HashSet<>();
        for (Supplier<? extends Card> supplier : getCardsUntilTurn(turn)) {
            Card card = supplier.get();
            if (CardUtils.isSpell(card)) {
                result.add((Spell) card);
            }
        }

        return result;
    }

    public List<Land> getLandCardsUntilTurn(int turn) {

        List<IdentifiedCardObject> landObjects = getCachedLandObjectsUntilTurn(turn);

        if (landObjects.isEmpty()) {
            return Collections.emptyList();
        }

        List<Land> result = new ArrayList<>(landObjects.size());
        for (IdentifiedCardObject cardObject : landObjects) {
            result.add((Land) cardObject.get());
        }

        return result;
    }

    public Collection<IdentifiedCardObject> getLandTypesInHandUntilTurn(int turn) {

        List<IdentifiedCardObject> landObjects = getCachedLandObjectsUntilTurn(turn);

        if (landObjects.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Card> cardTypes = new HashSet<>(landObjects.size());

        Collection<IdentifiedCardObject> result = new ArrayList<>(landObjects.size());
        for (IdentifiedCardObject cardObject : landObjects) {

            if (!cardObject.isPlayed() && cardTypes.add(cardObject.get())) {
                result.add(cardObject);
            }
        }

        return result;
    }

    private Iterable<IdentifiedCardObject> getCardsUntilTurn(int turn) {
        return Iterables.limit(_cards, _startingHandSize + turn - 1);
    }

    private List<IdentifiedCardObject> computeLandObjectsUntilTurn(int turn) {

        List<IdentifiedCardObject> result = new ArrayList<>();
        for (IdentifiedCardObject cardObject : getCardsUntilTurn(turn)) {

            if (CardUtils.isLand(cardObject.get())) {
                result.add(cardObject);
            }
        }

        return result;
    }

    private List<IdentifiedCardObject> getCachedLandObjectsUntilTurn(int turn) {

        turn = Math.min(turn, _cards.size() - _startingHandSize + 1);

        return _landObjectCache.computeIfAbsent(turn, this::computeLandObjectsUntilTurn);
    }

    @Override
    public String toString() {
        return _cards.toString();
    }

}
