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
import probability.core.Hand;
import probability.core.ManaCost;
import probability.core.Spell;
import probability.core.land.FetchLand;
import probability.core.land.Land;

class PlayableChecker {

    private Deck _deck;

    private Hand _hand;

    private Map<Color, Colors> _fetchableColors;

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
        Set<Color> fetchableColors = new HashSet<>(Color.numberOfColors());

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

        Set<Color> colors = new HashSet<>(Color.numberOfColors());

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

    private static class PlayableRecursion {

        private final Spell _spell;

        private final Hand _hand;

        private final int _maxTurn;

        public PlayableRecursion(Spell spell, Hand hand, int maxTurn) {
            _spell = spell;
            _hand = hand;
            _maxTurn = maxTurn;
        }

        private static ManaCost reduceCost(Color color, ManaCost spellColors) {
            ManaCost reducedCosts = new ManaCost(spellColors);

            if (reducedCosts.getCount(color) >= 1) {
                reducedCosts.removeColor(color);

            } else if (reducedCosts.getCount(Color.Colorless) >= 1) {
                reducedCosts.removeColor(Color.Colorless);
            }

            return reducedCosts;
        }

        public boolean check() {
            Board board = new Board();

            ManaCost cost = _spell.getCost();

            return recursion(board, null, cost, 1);
        }

        private boolean recursion(Board board, Color tappedColor,
                                  ManaCost remainingCost, int turn) {

            if (turn > _maxTurn) {
                return false;
            }

            if (tappedColor != null) {
                remainingCost = reduceCost(tappedColor, remainingCost);

                if (remainingCost.getCMC() == 0) {
                    return true;
                }
            }

            Set<Land> availableLandTypes = getAvailableLandTypes(board, turn);

            for (Land land : availableLandTypes) {

                final boolean tapped = land.comesIntoPlayTapped(board);

                board.playLand(land);

                for (Color color : land.producesColors(board)) {
                    if (!remainingCost.containsColor(color)) {
                        continue;
                    }

                    if (tapped) {
                        tappedColor = color;

                        if (recursion(board, tappedColor, remainingCost,
                                turn + 1)) {
                            return true;
                        }
                    } else {
                        remainingCost = reduceCost(color, remainingCost);

                        if (remainingCost.getCMC() == 0) {
                            return true;
                        }

                        if (recursion(board, null, remainingCost, turn + 1)) {
                            return true;
                        }
                    }

                }

                board.popLand();
            }

            return recursion(board, null, remainingCost, turn + 1);
        }

        private Set<Land> getAvailableLandTypes(Board board, int turn) {

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
