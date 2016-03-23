package probability.core.land;

import probability.checker.Board;
import probability.core.Colors;

public final class NonBasicLand extends AbstractLand {

    public NonBasicLand(String name, Colors colors) {
        super(name, colors);
    }

    @Override
    public boolean comesIntoPlayTapped(Board board) {
        return false;
    }

}
