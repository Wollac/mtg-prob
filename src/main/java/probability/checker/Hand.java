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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import probability.core.Card;
import probability.core.IdentifiedCardObject;
import probability.core.Spell;
import probability.core.land.Land;

final class Hand {

    private final int _startingHandSize;

    private final int _lastTurn;

    private final List<IdentifiedCardObject> _cards;

    private final Map<Integer, List<IdentifiedCardObject>> _landObjectsCache = new HashMap<>();

    Hand(Collection<IdentifiedCardObject> startingHand, Collection<IdentifiedCardObject> draws) {

        _startingHandSize = startingHand.size();

        _cards = new ArrayList<>(startingHand.size() + draws.size());
        _cards.addAll(startingHand);
        _cards.addAll(draws);

        _lastTurn = _cards.size() - _startingHandSize + 1;
    }

    Hand(int startingHandSize, Collection<? extends Card> cards) {

        assert startingHandSize >= 0 : startingHandSize;
        assert cards.size() >= startingHandSize;

        _startingHandSize = startingHandSize;
        _cards = IdentifiedCardObject.toCardObjects(cards);

        _lastTurn = _cards.size() - _startingHandSize + 1;
    }

    int size() {
        return _cards.size();
    }

    void markAllInHand() {

        _cards.stream().forEach(IdentifiedCardObject::markNotPlayed);
    }

    Set<Spell> getAllSpellTypes() {

        Set<Spell> result = new HashSet<>();

        for (IdentifiedCardObject o : _cards) {
            if (o.isSpell()) {
                result.add((Spell) o.get());
            }
        }

        return result;
    }

    List<Land> getAllLands() {

        List<IdentifiedCardObject> landObjects = getLandObjectsUntilTurn(_lastTurn);

        if (landObjects.isEmpty()) {
            return Collections.emptyList();
        }

        List<Land> result = new ArrayList<>(landObjects.size());
        for (IdentifiedCardObject cardObject : landObjects) {
            result.add((Land) cardObject.get());
        }

        return result;
    }

    List<IdentifiedCardObject> getNotPlayedLandObjectsUntilTurn(int turn) {

        List<IdentifiedCardObject> landObjects = getLandObjectsUntilTurn(turn);

        if (landObjects.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Card> cardTypes = new HashSet<>(landObjects.size());

        List<IdentifiedCardObject> result = new ArrayList<>(landObjects.size());
        for (IdentifiedCardObject cardObject : landObjects) {

            if (!cardObject.isPlayed() && cardTypes.add(cardObject.get())) {
                result.add(cardObject);
            }
        }

        return result;
    }

    int getLastLandTurn() {

        int turn = _lastTurn;
        for (ListIterator<IdentifiedCardObject> it = _cards.listIterator(_cards.size()); it.hasPrevious(); turn--) {

            if (turn <= 1) {
                return 1;
            }
            if (it.previous().isLand()) {
                return turn;
            }
        }

        assert true;
        return 0;
    }

    private List<IdentifiedCardObject> getLandObjectsUntilTurn(int turn) {

        turn = Math.min(_lastTurn, turn);
        return _landObjectsCache.computeIfAbsent(turn, this::createLandObjectsUntilTurn);
    }

    private int getNumberOfCardsUntilTurn(int turn) {
        return Math.min(_cards.size(), _startingHandSize + turn - 1);
    }

    private List<IdentifiedCardObject> createLandObjectsUntilTurn(int turn) {

        final int n = getNumberOfCardsUntilTurn(turn);

        List<IdentifiedCardObject> result = new ArrayList<>();

        Iterator<IdentifiedCardObject> it = _cards.iterator();
        for (int i = 0; i < n; i++) {
            IdentifiedCardObject cardObject = it.next();
            if (cardObject.isLand()) {
                result.add(cardObject);
            }
        }

        return result;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("starting=").append(_cards.subList(0, _startingHandSize));

        // we need a mutable Object
        AtomicInteger index = new AtomicInteger(2);
        String draws = _cards.stream().skip(_startingHandSize).map(c -> index.getAndIncrement() + "->" + c).collect(Collectors.joining(", "));

        sb.append(", draws=").append('{').append(draws).append('}');

        return sb.toString();
    }

}
