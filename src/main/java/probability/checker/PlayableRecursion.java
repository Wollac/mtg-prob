package probability.checker;

import probability.core.Board;
import probability.core.Color;
import probability.core.ManaCost;
import probability.core.land.Land;
import probability.utils.EnumCount;

class PlayableRecursion {

    private final Hand _hand;

    private final int _maxTurn;

    private final PlayableCache _cache;

    private final RemainingManaCost remainingCost;

    private final Board board;

    PlayableRecursion(Hand hand, int maxTurn, ManaCost spellCost) {

        assert maxTurn > 0 : maxTurn;

        _hand = hand;
        _maxTurn = maxTurn;
        _cache = new PlayableCache(maxTurn + 1);

        remainingCost = new RemainingManaCost(spellCost);
        board = new Board();
    }

    boolean check() {

        final int turn = 1;
        return recursion(turn);
    }

    private boolean recursion(int turn) {

        if (turn > _maxTurn) {
            return false;
        }
        if (remainingCost.allPaid()) {
            return true;
        }

        if (isUselessSituation(turn)) {
            return false;
        }

        for (IdentifiedCardObject frame : _hand.getLandTypesInHandUntilTurn(turn)) {

            final Land land = (Land) frame.get();
            final boolean tapped = land.comesIntoPlayTapped(board);

            Iterable<Color> colors = land.producesColors(board);

            playLandObject(frame);

            for (Color color : colors) {

                if (remainingCost.contains(color)) {

                    if (tapped) {
                        if (addTappedColorAndTest(color, turn)) return true;
                    } else {
                        if (addColorAndTest(color, turn)) return true;
                    }
                }

            }

            removeLandObject(frame);
        }

        // do not play any lands this turn
        if (recursion(turn + 1)) return true;

        cacheUselessSituation(turn);

        return false;
    }

    private boolean addColorAndTest(Color color, int currentTurn) {

        remainingCost.payMana(color);
        if (remainingCost.allPaid()) {
            return true;
        }

        if (currentTurn < _maxTurn && recursion(currentTurn + 1)) {
            return true;
        }
        remainingCost.freeMana(color);

        return false;
    }

    private boolean addTappedColorAndTest(Color color, int currentTurn) {

        if (currentTurn < _maxTurn) {
            remainingCost.payMana(color);

            if (recursion(currentTurn + 1)) {
                return true;
            }

            remainingCost.freeMana(color);
        }

        return false;
    }

    private void playLandObject(IdentifiedCardObject landObject) {

        board.playLand((Land) landObject.get());
        landObject.markPlayed();
    }

    private void removeLandObject(IdentifiedCardObject landObject) {

        landObject.markNotPlayed();
        Land popLand = board.popLand();

        // the popped Land should always be the one from the IdentifiedCardObject
        assert landObject.get() == popLand;
    }

    private boolean isUselessSituation(int turn) {
        return _cache.contains(board, _maxTurn - turn);
    }

    private void cacheUselessSituation(int turn) {

        final int remainingTurns = _maxTurn - turn;

        // this situation must not already be contained in the cache
        assert !_cache.contains(board, remainingTurns);

        _cache.add(board, remainingTurns);
    }

    private static final class RemainingManaCost {

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
            assert _spentGenericMana <= _originalCost.genericCount();
        }

        public void freeMana(Color color) {

            final int oldCount = _manaPool.decrease(color) + 1;

            if (_originalCost.count(color) < oldCount) {
                _spentGenericMana--;
            }

            // we will never free mana that has not been paid
            assert oldCount > 0;
        }

        public boolean allPaid() {

            return _originalCost.getConverted() <= _manaPool.totalCount();
        }

        public boolean contains(Color color) {

            final int remainingGeneric = _originalCost.genericCount() - _spentGenericMana;

            return remainingGeneric > 0 || _originalCost.count(color) > _manaPool.count(color);
        }

        public ManaCost computeRemainingManaCost() {

            EnumCount<Color> remainingColors = new EnumCount<>(Color.class);
            for (Color c : Color.values()) {
                remainingColors.increase(c, _originalCost.count(c) - _manaPool.count(c));
            }

            return new ManaCost(remainingColors, _originalCost.genericCount() - _spentGenericMana);
        }

        @Override
        public String toString() {
            return computeRemainingManaCost().toString();
        }
    }

}
