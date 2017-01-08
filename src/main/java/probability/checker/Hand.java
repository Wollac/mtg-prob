package probability.checker;

import probability.core.Card;
import probability.core.CardObject;
import probability.core.ManaCost;
import probability.core.Spell;

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

final class Hand {

  private final int _startingHandSize;

  private final int _lastTurn;

  private final List<CardObject> _cards;

  private final Map<Integer, List<CardObject>> _landObjectsCache = new HashMap<>();

  Hand(Collection<CardObject> startingHand, Collection<CardObject> draws) {

    _startingHandSize = startingHand.size();

    _cards = new ArrayList<>(startingHand.size() + draws.size());
    _cards.addAll(startingHand);
    _cards.addAll(draws);

    _lastTurn = getLastTurn();
  }

  Hand(int startingHandSize, Collection<? extends Card> cards) {

    assert startingHandSize >= 0 : startingHandSize;
    assert cards.size() >= startingHandSize;

    _startingHandSize = startingHandSize;
    _cards = CardObject.toCardObjects(cards);

    _lastTurn = getLastTurn();
  }

  private int getLastTurn() {
    return _cards.size() - _startingHandSize + 1;
  }

  int size() {
    return _cards.size();
  }

  void markAllInHand() {

    _cards.forEach(CardObject::markNotPlayed);
  }

  /**
   * Returns the mana costs corresponding to all the spells in the current hand.
   */
  Set<ManaCost> getAllSpellCosts() {

    Set<ManaCost> result = new HashSet<>();

    for (CardObject o : _cards) {
      if (o.isSpell()) {
        Spell spell = (Spell) o.get();
        result.add(spell.getCost());
      }
    }

    return result;
  }

  List<CardObject> getAllLandObjects() {

    return getLandObjectsUntilTurn(_lastTurn);
  }

  List<CardObject> getNotPlayedLandObjectsUntilTurn(int turn) {

    List<CardObject> landObjects = getLandObjectsUntilTurn(turn);

    if (landObjects.isEmpty()) {
      return Collections.emptyList();
    }

    Set<Card> cardTypes = new HashSet<>(landObjects.size());

    List<CardObject> result = new ArrayList<>(landObjects.size());
    for (CardObject cardObject : landObjects) {

      if (!cardObject.isPlayed() && cardTypes.add(cardObject.get())) {
        result.add(cardObject);
      }
    }

    return result;
  }

  int getLastLandTurn() {

    int turn = _lastTurn;
    for (ListIterator<CardObject> it = _cards.listIterator(_cards.size()); it
        .hasPrevious(); turn--) {

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

  private List<CardObject> getLandObjectsUntilTurn(int turn) {

    turn = Math.min(_lastTurn, turn);
    return _landObjectsCache.computeIfAbsent(turn, this::createLandObjectsUntilTurn);
  }

  private int getNumberOfCardsUntilTurn(int turn) {
    return Math.min(_cards.size(), _startingHandSize + turn - 1);
  }

  private List<CardObject> createLandObjectsUntilTurn(int turn) {

    final int n = getNumberOfCardsUntilTurn(turn);

    List<CardObject> result = new ArrayList<>();

    Iterator<CardObject> it = _cards.iterator();
    for (int i = 0; i < n; i++) {
      CardObject cardObject = it.next();
      if (cardObject.isLand()) {
        result.add(cardObject);
      }
    }

    return result;
  }

  @Override public String toString() {

    StringBuilder sb = new StringBuilder();

    sb.append("starting=").append(_cards.subList(0, _startingHandSize));

    // we need a mutable Object
    AtomicInteger index = new AtomicInteger(2);
    String draws =
        _cards.stream().skip(_startingHandSize).map(c -> index.getAndIncrement() + "->" + c)
            .collect(Collectors.joining(", "));

    sb.append(", draws=").append('{').append(draws).append('}');

    return sb.toString();
  }

}
