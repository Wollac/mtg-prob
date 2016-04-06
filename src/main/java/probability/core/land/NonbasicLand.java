package probability.core.land;

import probability.core.Board;
import probability.core.Colors;

/**
 * Models Nonbasic Lands such as <a href="http://magiccards.info/query?q=Adarkar+Wastes">Adarkar Wastes</a>.
 * <p>
 * Nonbasic Lands are lands that don't have the basic supertype but never enter the battlefield tapped.
 */
public final class NonbasicLand extends AbstractLand {

    public NonbasicLand(String name, Colors colors) {
        super(name, colors);
    }

    @Override
    public boolean comesIntoPlayTapped(Board board) {
        return false;
    }

}
