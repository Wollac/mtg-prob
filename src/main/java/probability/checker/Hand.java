package probability.checker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import probability.core.Card;
import probability.core.CardUtils;
import probability.core.IdentifiedCardObject;
import probability.core.Spell;
import probability.core.land.Land;

class Hand {

    private final int _startingHandSize;
    private final List<IdentifiedCardObject> _cards;
    private final Map<Integer, List<IdentifiedCardObject>> _landObjectCache = new HashMap<>();

    Hand(Collection<IdentifiedCardObject> startingHand, Collection<IdentifiedCardObject> draws) {

        _startingHandSize = startingHand.size();

        _cards = new ArrayList<>(startingHand.size() + draws.size());
        _cards.addAll(startingHand);
        _cards.addAll(draws);
    }

    Hand(int startingHandSize, Collection<? extends Card> cards) {

        assert startingHandSize >= 0 : startingHandSize;
        assert cards.size() >= startingHandSize;

        _startingHandSize = startingHandSize;
        _cards = IdentifiedCardObject.toCardObjects(cards);
    }

    int size() {
        return _cards.size();
    }

    void markAllInHand() {

        _cards.stream().forEach(IdentifiedCardObject::markNotPlayed);
    }

    Set<Spell> getSpellTypesUntilTurn(int turn) {

        Set<Spell> result = new HashSet<>();

        final int n = getNumberOfCardsUntilTurn(turn);

        Iterator<IdentifiedCardObject> it = _cards.iterator();
        for (int i = 0; i < n; i++) {
            Card card = it.next().get();
            if (CardUtils.isSpell(card)) {
                result.add((Spell) card);
            }
        }

        return result;
    }

    List<Land> getLandCardsUntilTurn(int turn) {

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

    Collection<IdentifiedCardObject> getLandTypesInHandUntilTurn(int turn) {

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

    int getLastLandTurn() {

        int turn = _cards.size() - _startingHandSize + 1;
        for (ListIterator<IdentifiedCardObject> it = _cards.listIterator(_cards.size()); it.hasPrevious(); turn--) {

            if (turn <= 1) {
                return 1;
            }

            if (CardUtils.isLand(it.previous().get())) {
                return turn;
            }
        }

        assert true;
        return 0;
    }

    private int getNumberOfCardsUntilTurn(int turn) {
        return Math.min(_cards.size(), _startingHandSize + turn - 1);
    }

    private List<IdentifiedCardObject> getLandObjects(int n) {

        List<IdentifiedCardObject> result = new ArrayList<>();

        Iterator<IdentifiedCardObject> it = _cards.iterator();
        for (int i = 0; i < n; i++) {
            IdentifiedCardObject cardObject = it.next();
            if (CardUtils.isLand(cardObject.get())) {
                result.add(cardObject);
            }
        }

        return result;
    }

    private List<IdentifiedCardObject> getCachedLandObjectsUntilTurn(int turn) {

        final int n = getNumberOfCardsUntilTurn(turn);

        return _landObjectCache.computeIfAbsent(n, this::getLandObjects);
    }

    @Override
    public String toString() {
        return _cards.toString();
    }

}
