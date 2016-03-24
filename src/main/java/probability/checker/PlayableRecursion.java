package probability.checker;

import probability.core.Board;
import probability.core.Color;
import probability.core.EnumCount;
import probability.core.ManaCost;
import probability.core.land.Land;

class PlayableRecursion {

    private final Hand _hand;

    private final int _maxTurn;

    private final RemainingManaCost remainingCost;

    private final Board board;

    public PlayableRecursion(Hand hand, int maxTurn, ManaCost spellCost) {

        _hand = hand;
        _maxTurn = maxTurn;

        remainingCost = new RemainingManaCost(spellCost);
        board = new Board();
    }

    public boolean check() {

        final Color tappedColor = null;
        final int turn = 1;

        return recursion(tappedColor, turn);
    }

    private boolean recursion(Color tappedColor, int turn) {

        if (turn > _maxTurn) {
            return false;
        }

        if (tappedColor != null) {

            remainingCost.payMana(tappedColor);
            if (remainingCost.allPaid()) {
                return true;
            }
        }

        for (CardObject frame : _hand.getUnplayedLandTypesUntilTurn(turn)) {

            final Land land = (Land) frame.get();
            final boolean tapped = land.comesIntoPlayTapped(board);

            Iterable<Color> colors = land.producesColors(board);

            board.playLand(land);
            frame.markPlayed();

            for (Color color : colors) {

                if (remainingCost.contains(color)) {

                    if (tapped) {
                        if (testColor(color, turn)) return true;
                    } else {
                        if (testTappedColor(color, turn)) return true;
                    }
                }

            }

            board.popLand();
            frame.markNotPlayed();
        }

        return recursion(null, turn + 1);
    }

    private boolean testTappedColor(Color color, int currentTurn) {

        remainingCost.payMana(color);
        if (remainingCost.allPaid()) {
            return true;
        }

        if (currentTurn < _maxTurn && recursion(null, currentTurn + 1)) {
            return true;
        }
        remainingCost.freeMana(color);

        return false;
    }

    private boolean testColor(Color color, int currentTurn) {

        if (currentTurn < _maxTurn) {

            if (recursion(color, currentTurn + 1)) {
                return true;
            }

            // color has only been considered in the remaining cost if there was time to spend it
            remainingCost.freeMana(color);
        }

        return false;
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

}
