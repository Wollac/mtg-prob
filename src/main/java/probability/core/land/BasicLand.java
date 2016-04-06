package probability.core.land;

import probability.core.Board;
import probability.core.Colors;

/**
 * Models Basic Lands such as <a href="http://magiccards.info/query?q=!Tundra">Tundra</a>.
 * <p>
 * Basic Lands represent a combination of the basic types Plains, Island, Swamp, Mountain or Forest and
 * never enter the battlefield tapped.
 */
public final class BasicLand extends AbstractLand {

    public BasicLand(String name, Colors colors) {
        super(name, colors);
    }

    @Override
    public boolean comesIntoPlayTapped(Board board) {
        return false;
    }

}
