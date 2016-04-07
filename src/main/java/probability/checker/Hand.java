package probability.checker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import probability.core.Card;
import probability.core.CardUtils;
import probability.core.IdentifiedCardObject;
import probability.core.Spell;
import probability.core.land.Land;
import probability.utils.FixedCapacityIntCache;

final class Hand {

    private final int _startingHandSize;

    private final List<IdentifiedCardObject> _cards;

    private final FixedCapacityIntCache<List<IdentifiedCardObject>> _landObjectsCache;

    Hand(Collection<IdentifiedCardObject> startingHand, Collection<IdentifiedCardObject> draws) {

        _startingHandSize = startingHand.size();

        _cards = new ArrayList<>(startingHand.size() + draws.size());
        _cards.addAll(startingHand);
        _cards.addAll(draws);

        _landObjectsCache = createLandObjectsTurnCache();
    }

    Hand(int startingHandSize, Collection<? extends Card> cards) {

        assert startingHandSize >= 0 : startingHandSize;
        assert cards.size() >= startingHandSize;

        _startingHandSize = startingHandSize;
        _cards = IdentifiedCardObject.toCardObjects(cards);

        _landObjectsCache = createLandObjectsTurnCache();
    }

    int size() {
        return _cards.size();
    }

    void markAllInHand() {

        _cards.stream().forEach(IdentifiedCardObject::markNotPlayed);
    }

    Set<Spell> getAllSpellTypes() {

        Set<Spell> result = new HashSet<>();

        for (IdentifiedCardObject cardObject : _cards) {
            Card card = cardObject.get();
            if (CardUtils.isSpell(card)) {
                result.add((Spell) card);
            }
        }

        return result;
    }

    List<Land> getAllLands() {

        List<IdentifiedCardObject> landObjects = getLandObjectsUntilTurn(getTurnWithLastDraw());

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

        int turn = getTurnWithLastDraw();
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

    private FixedCapacityIntCache<List<IdentifiedCardObject>> createLandObjectsTurnCache() {

        return new FixedCapacityIntCache<>(getTurnWithLastDraw() + 1, this::createLandObjectsUntilTurn);
    }

    private List<IdentifiedCardObject> getLandObjectsUntilTurn(int turn) {

        turn = Math.min(getTurnWithLastDraw(), turn);
        return _landObjectsCache.get(turn);
    }

    private int getTurnWithLastDraw() {
        return _cards.size() - _startingHandSize + 1;
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
            if (CardUtils.isLand(cardObject.get())) {
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
