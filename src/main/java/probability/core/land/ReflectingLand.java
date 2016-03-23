package probability.core.land;

import com.google.common.collect.Sets;

import java.util.Set;

import probability.checker.Board;
import probability.core.Color;
import probability.core.Colors;

public final class ReflectingLand extends AbstractLand {

    public ReflectingLand(String name, Colors colors) {
        super(name, colors);
    }

    @Override
    public Set<Color> producesColors(Board board) {
        return Sets.intersection(board.getPlayedLandProducibleColors(), colors());
    }

    @Override
    public boolean comesIntoPlayTapped(Board board) {
        return false;
    }

}
