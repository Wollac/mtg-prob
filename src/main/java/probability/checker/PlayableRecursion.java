package probability.checker;

import java.util.Set;

import probability.core.Board;
import probability.core.Color;
import probability.core.EnumCount;
import probability.core.ManaCost;
import probability.core.Spell;
import probability.core.land.Land;

class PlayableRecursion {

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

        Set<PlayableLand> availableLandTypes = _hand.getPlayableLandTypesUtilTurn(turn);

        for (PlayableLand frame : availableLandTypes) {

            Land land = frame.getLand();

            final boolean tapped = land.comesIntoPlayTapped(board);

            Set<Color> colors = land.producesColors(board);
            board.playLand(frame);

            for (Color color : colors) {

                if (!remainingCost.contains(color)) {
                    continue;
                }

                if (tapped) {

                    if (turn < _maxTurn) {

                        tappedColor = color;

                        if (recursion(board, tappedColor, remainingCost, turn + 1)) {
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

}
