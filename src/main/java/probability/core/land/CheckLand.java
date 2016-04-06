package probability.core.land;

import probability.core.Board;
import probability.core.Colors;

/**
 * Models Check Lands such as <a href="http://magiccards.info/query?q=!Glacial+Fortress">Glacial+Fortress</a>.
 * <p>
 * Check lands enter the battlefield tapped unless there is also a {@linkplain BasicLand} on the board that shares a
 * color with the check land.
 */
public class CheckLand extends AbstractLand {

    public CheckLand(String name, Colors colors) {
        super(name, colors);
    }

    @Override
    public boolean comesIntoPlayTapped(Board board) {
        return !board.isBasicColorPlayed(colors());
    }

}
