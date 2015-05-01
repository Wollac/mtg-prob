package probability.core.land;

import probability.core.Board;
import probability.core.Color;
import probability.core.Colors;

public class NonBasicLand extends AbstractLand implements Land {

	public NonBasicLand(String name, Colors colors) {
		super(name, colors);
	}

	@Override
	public boolean comesIntoPlayTapped(Board board) {
		return false;
	}

	@Override
	public boolean isFetchable(Color color) {
		return false;
	}

}
