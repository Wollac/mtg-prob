package probability.checker;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import probability.checker.PlayableLand;
import probability.core.CardUtils;
import probability.core.Color;

public class Board {

    private final Stack<PlayableLand> _playedLands;

    public Board() {
        _playedLands = new Stack<>();
    }

    public void playLand(PlayableLand frame) {
        frame.markPlayed();
        _playedLands.push(frame);
    }

    public void popLand() {
        _playedLands.pop().markNotPlayed();
    }

    public int getNumPlayedLands() {
        return _playedLands.size();
    }

    public Set<Color> getPlayedLandProducibleColors() {
        Set<Color> colors = new HashSet<>();

        for (PlayableLand frame : _playedLands) {
            colors.addAll(frame.getLand().producibleColors());
        }

        return colors;
    }

    public boolean isBasicColorPlayed(Set<Color> colors) {
        return !Collections.disjoint(colors, getPlayedBasicLandColors());
    }

    private Set<Color> getPlayedBasicLandColors() {

        Set<Color> colors = Color.emptyEnumSet();

        for (PlayableLand frame : _playedLands) {
            if (CardUtils.isBasicLand(frame.getLand())) {
                colors.addAll(frame.getLand().colors());
            }
        }

        return colors;
    }

}
