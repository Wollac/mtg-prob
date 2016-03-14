package probability.core.land;

import probability.core.Board;
import probability.core.Colors;

public final class FastLand extends AbstractLand {

    public FastLand(String name, Colors colors) {
        super(name, colors);
    }

    @Override
    public boolean comesIntoPlayTapped(Board board) {
        return board.getNumPlayedLands() > 2;
    }

}
