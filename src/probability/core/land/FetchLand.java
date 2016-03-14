package probability.core.land;

import probability.core.Board;
import probability.core.Color;
import probability.core.Colors;

import java.util.Set;

public final class FetchLand extends AbstractLand {

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
