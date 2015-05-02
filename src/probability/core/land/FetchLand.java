package probability.core.land;

import java.util.Set;

import probability.core.Board;
import probability.core.Color;
import probability.core.Colors;

public class FetchLand extends AbstractLand {

	private Colors _fetchedColors;

	public FetchLand(String name, Colors colors) {
		super(name, colors);

		_fetchedColors = new Colors();
	}

	public void setFetchableColors(Colors colors) {
		_fetchedColors = new Colors(colors);
	}

	@Override
	public Set<Color> producesColors() {
		return _fetchedColors.getColors();
	}

	@Override
	public boolean comesIntoPlayTapped(Board board) {
		return false;
	}

}
