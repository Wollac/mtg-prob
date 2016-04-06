package probability.core.land;

import probability.core.Board;
import probability.core.Colors;

/**
 * Models Fast Lands such as <a href="http://magiccards.info/query?q=!Seachrome+Coast">Seachrome Coast</a>.
 * <p>
 * Fast Lands enter the battlefield tapped unless unless not more than two other lands are on the board.
 */
public final class FastLand extends AbstractLand {

    public FastLand(String name, Colors colors) {
        super(name, colors);
    }

    @Override
    public boolean comesIntoPlayTapped(Board board) {
        return board.getNumPlayedLands() > 2;
    }

}
