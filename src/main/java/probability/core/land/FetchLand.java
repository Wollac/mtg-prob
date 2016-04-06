package probability.core.land;

import probability.core.Board;
import probability.core.Color;
import probability.core.Colors;

import java.util.Set;

/**
 * Models Fetch Lands such as <a href="http://magiccards.info/query?q=!Flooded+Strand">Flooded Strand</a>.
 * <p>
 * Fetch lands can produce the colors of all {@linkplain BasicLand}s that are not yet played or drawn.
 * They never enter the battlefield tapped.
 */
public class FetchLand extends AbstractLand {

    private Colors _fetchedColors;

    public FetchLand(String name, Colors colors) {

        super(name, colors);

        _fetchedColors = new Colors();
    }

    public void setFetchableColors(Set<Color> colors) {
        _fetchedColors = new Colors(colors);
    }

    @Override
    public Set<Color> producibleColors() {
        return _fetchedColors.getColors();
    }

    @Override
    public Set<Color> producesColors(Board board) {
        return _fetchedColors.getColors();
    }

    @Override
    public boolean comesIntoPlayTapped(Board board) {
        return false;
    }

}
