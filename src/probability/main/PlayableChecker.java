package probability.main;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import probability.core.Board;
import probability.core.Card;
import probability.core.CardUtils;
import probability.core.Color;
import probability.core.Colors;
import probability.core.Deck;
import probability.core.EnumCount;
import probability.core.Hand;
import probability.core.ManaCost;
import probability.core.Spell;
import probability.core.land.FetchLand;
import probability.core.land.Land;

class PlayableChecker {

    private final Deck _deck;

    private final Hand _hand;

    private final Map<Color, Colors> _fetchableColors;

    public PlayableChecker(Deck deck, Hand hand) {
        _deck = deck;
        _hand = hand;

        _fetchableColors = new HashMap<>();
    }

    public boolean isPlayable(int turn) {

        Collection<Spell> spells = CardUtils.retainAllSpellsToArrayList(_hand.getCardsUntilTurn(turn));

        if (spells.isEmpty()) {
            return true;
        }

        Collection<Land> lands = CardUtils.retainAllLandsToArrayList(_hand.getCardsUntilTurn(turn));

        initializeFetchLands(lands);

        Set<Spell> playableSpellTypes = getPlayableSpells(spells, turn, lands);

        for (Spell spell : playableSpellTypes) {

            PlayableRecursion recursion = new PlayableRecursion(spell, _hand, turn);

            if (recursion.check()) {
                return true;
            }
        }

        return false;
    }

    private void initializeFetchLands(Collection<Land> lands) {

        for (Land land : lands) {
            if (CardUtils.isFetchLand(land)) {
                FetchLand fetch = (FetchLand) land;

                fetch.setFetchableColors(getFetchableColors(fetch.colors()));
            }
        }
    }

    private Set<Color> getFetchableColors(Set<Color> colors) {

        Set<Color> fetchableColors = Color.emptyEnumSet();

        for (Color color : colors) {

            Colors currentFetchableColors = _fetchableColors.get(color);

            if (currentFetchableColors == null) {

                currentFetchableColors = computeFetchableColors(color);
                _fetchableColors.put(color, currentFetchableColors);
            }

            fetchableColors.addAll(currentFetchableColors.getColors());
        }

        return fetchableColors;
    }

    private Colors computeFetchableColors(Color color) {
        Collection<Land> remainingLands = CardUtils.retainAllLandsToArrayList(getRemainingCards());

        Set<Land> remainingLandTypes = new HashSet<>(remainingLands);

        Set<Color> colors = Color.emptyEnumSet();

        for (Land land : remainingLandTypes) {
            if (CardUtils.isBasicLand(land) && land.colors().contains(color)) {
                colors.addAll(land.producibleColors());
            }
        }

        return new Colors(colors);
    }

    private Collection<Card> getRemainingCards() {
        List<Card> allCards = _deck.cards();

        return allCards.subList(_hand.size(), allCards.size());
    }

    private Set<Spell> getPlayableSpells(Collection<Spell> spells, int turn,
                                         Collection<Land> lands) {

        Set<Spell> result = new HashSet<>(spells);

        for (Iterator<Spell> iterator = result.iterator(); iterator.hasNext(); ) {
            Spell spell = iterator.next();

            if (spell.getCMC() > turn || spell.getCMC() > lands.size()) {
                iterator.remove();
            }
        }

        return result;
    }

    private static class RemainingManaCost {

        private final ManaCost _originalCost;

        private final EnumCount<Color> _manaPool;

        private int _spentGenericMana;

        RemainingManaCost(ManaCost cost) {
            _originalCost = cost;

            _manaPool = new EnumCount<>(Color.class);
            _spentGenericMana = 0;
        }

        public void payMana(Color color) {

            final int newCount = _manaPool.increase(color);

            if (_originalCost.count(color) < newCount) {
                _spentGenericMana++;
            }

            // we will never pay more than we could
            if (_spentGenericMana > _originalCost.genericCount()) {
                throw new IllegalStateException();
            }
        }

        public void freeMana(Color color) {

            final int oldCount = _manaPool.decrease(color) + 1;

            if (_originalCost.count(color) < oldCount) {
                _spentGenericMana--;
            }

            // we will never free mana that has not been paid
            if (oldCount == 0) {
                throw new IllegalStateException();
            }
        }

        public boolean allPaid() {

            return _originalCost.getConverted() <= _manaPool.totalCount();
        }

        public boolean contains(Color color) {

            final int remainingGeneric = _originalCost.genericCount() - _spentGenericMana;

            return remainingGeneric > 0 || _originalCost.count(color) > _manaPool.count(color);
        }
    }

    private static class PlayableRecursion {

        private final Spell _spell;

        private final Hand _hand;

        private final int _maxTurn;

        public PlayableRecursion(Spell spell, Hand hand, int maxTurn) {
            _spell = spell;
            _hand = hand;
            _maxTurn = maxTurn;
        }

        public boolean check() {

            Board board = new Board();
            RemainingManaCost cost = new RemainingManaCost(_spell.getCost());
            final Color tappedColor = null;
            final int turn = 1;

            return recursion(board, tappedColor, cost, turn);
        }

        private boolean recursion(Board board, Color tappedColor,
                                  RemainingManaCost remainingCost, int turn) {

            if (turn > _maxTurn) {
                return false;
            }

            if (tappedColor != null) {

                remainingCost.payMana(tappedColor);
                if (remainingCost.allPaid()) {
                    return true;
                }
            }

            Set<Land> availableLandTypes = getAvailableLandTypes(turn);

            for (Land land : availableLandTypes) {

                final boolean tapped = land.comesIntoPlayTapped(board);

                board.playLand(land);

                for (Color color : land.producesColors(board)) {

                    if (!remainingCost.contains(color)) {
                        continue;
                    }

                    if (tapped) {

                        if (turn < _maxTurn) {

                            tappedColor = color;

                            if (recursion(board, tappedColor, remainingCost,
                                    turn + 1)) {
                                return true;
                            }
                            remainingCost.freeMana(tappedColor);
                        }
                    } else {

                        remainingCost.payMana(color);
                        if (remainingCost.allPaid()) {
                            return true;
                        }

                        if (recursion(board, null, remainingCost, turn + 1)) {
                            return true;
                        }
                        remainingCost.freeMana(color);

                    }

                }

                board.popLand();
            }

            return recursion(board, null, remainingCost, turn + 1);
        }

        private Set<Land> getAvailableLandTypes(int turn) {

            Set<Land> result = new HashSet<>();

            for (Card card : _hand.getCardsUntilTurn(turn)) {
                if (card instanceof Land) {
                    result.add((Land) card);
                }
            }

            return result;
        }
    }

}
