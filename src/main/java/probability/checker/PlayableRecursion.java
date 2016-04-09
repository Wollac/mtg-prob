package probability.checker;

import probability.core.Board;
import probability.core.CardObject;
import probability.core.Color;
import probability.core.ManaCost;
import probability.core.land.Land;
import probability.utils.EnumCount;

class PlayableRecursion {

    private final Hand _hand;

    private final int _maxTurn;

    private final int _lastLandTurn;

    private final RemainingManaCost remainingCost;

    private final Board board;

    PlayableRecursion(Hand hand, int maxTurn, ManaCost spellCost) {

        assert maxTurn > 0 : maxTurn;

        _hand = hand;

        _maxTurn = maxTurn;
        _lastLandTurn = Math.min(_maxTurn, hand.getLastLandTurn());

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

        boolean noLandProducesUsableColors = true;
        for (CardObject landObject : _hand.getNotPlayedLandObjectsUntilTurn(turn)) {

            final Land land = (Land) landObject.get();
            final boolean tapped = land.comesIntoPlayTapped(board);

            Iterable<Color> colors = land.producesColors();

            playLandObject(landObject);

            boolean producesUsableColors = false;
            for (Color color : colors) {

                if (remainingCost.contains(color)) {

                    producesUsableColors = true;
                    if (tapped) {
                        if (addTappedColorAndTest(color, turn)) return true;
                    } else {
                        if (addColorAndTest(color, turn)) return true;
                    }
                }

            }

            // it makes never sense to not use a usable land
            if (!producesUsableColors) {
                // play the land but don't use the mana
                if (recursion(turn + 1)) return true;
            } else {
                noLandProducesUsableColors = false;
            }

            removeLandObject(landObject);
        }

        // as a land can produce at most one mana, it makes never sense to not play a land that could have been used
        // it also makes no sense, if there wont be any land available later on
        if (noLandProducesUsableColors && turn < _lastLandTurn) {
            // do not play any lands this turn
            if (recursion(turn + 1)) return true;
        }

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

    private void playLandObject(CardObject landObject) {

        board.play(landObject);
        landObject.markPlayed();
    }

    private void removeLandObject(CardObject landObject) {

        landObject.markNotPlayed();
        CardObject popObject = board.pop();

        // the popped Object should always be the the CardObject
        assert popObject == landObject;
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

        void payMana(Color color) {

            final int newCount = _manaPool.increase(color);

            if (_originalCost.count(color) < newCount) {
                _spentGenericMana++;
            }

            // we will never pay more than we could
            assert _spentGenericMana <= _originalCost.genericCount();
        }

        void freeMana(Color color) {

            final int oldCount = _manaPool.decrease(color) + 1;

            if (_originalCost.count(color) < oldCount) {
                _spentGenericMana--;
            }

            // we will never free mana that has not been paid
            assert oldCount > 0;
        }

        boolean allPaid() {

            return _originalCost.getConverted() <= _manaPool.totalCount();
        }

        boolean contains(Color color) {

            final int remainingGeneric = _originalCost.genericCount() - _spentGenericMana;

            return remainingGeneric > 0 || _originalCost.count(color) > _manaPool.count(color);
        }

        private ManaCost computeRemainingManaCost() {

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
