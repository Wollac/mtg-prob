package probability.core.land;

import probability.checker.Board;
import probability.core.Colors;

public final class TapLand extends AbstractLand {

    public TapLand(String name, Colors colors) {
        super(name, colors);
    }

    @Override
    public boolean comesIntoPlayTapped(Board board) {
        return true;
    }

}
