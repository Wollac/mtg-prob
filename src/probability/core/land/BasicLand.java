package probability.core.land;

import probability.core.Board;
import probability.core.Colors;

public final class BasicLand extends AbstractLand {

    public BasicLand(String name, Colors colors) {
        super(name, colors);
    }

    @Override
    public boolean comesIntoPlayTapped(Board board) {
        return false;
    }

}
