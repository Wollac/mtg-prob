package probability.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Board {

    private final Stack<Hand.Frame> _playedLands;

    public Board() {
        _playedLands = new Stack<>();
    }

    public void playLand(Hand.Frame frame) {
        frame.markPlayed();
        _playedLands.push(frame);
    }

    public void popLand() {
        _playedLands.pop().markPlayed();
    }

    public int getNumPlayedLands() {
        return _playedLands.size();
    }

    public Set<Color> getPlayedLandProducibleColors() {
        Set<Color> colors = new HashSet<>();

        for (Hand.Frame frame : _playedLands) {
            colors.addAll(frame.getCard().producibleColors());
        }

        return colors;
    }

    public boolean isBasicColorPlayed(Set<Color> colors) {
        return !Collections.disjoint(colors, getPlayedBasicLandColors());
    }

    private Set<Color> getPlayedBasicLandColors() {

        Set<Color> colors = Color.emptyEnumSet();

        for (Hand.Frame frame : _playedLands) {
            if (CardUtils.isBasicLand(frame.getCard())) {
                colors.addAll(frame.getCard().colors());
            }
        }

        return colors;
    }

}
