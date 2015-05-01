package probability.core.land;

import probability.core.Board;
import probability.core.Color;
import probability.core.Colors;

public class BasicLand extends AbstractLand {

	public BasicLand(String name, Colors colors) {
		super(name, colors);
	}

	@Override
	public boolean comesIntoPlayTapped(Board board) {
		return false;
	}

	@Override
	public boolean isFetchable(Color color) {
		if (color != Color.Colorless && colors().contains(color)) {
			return true;
		}

		return false;
	}

}
