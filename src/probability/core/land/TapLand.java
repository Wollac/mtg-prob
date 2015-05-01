package probability.core.land;

import probability.core.Board;
import probability.core.Color;
import probability.core.Colors;

public class TapLand extends AbstractLand implements Land {

	public TapLand(String name, Colors colors) {
		super(name, colors);
	}

	@Override
	public boolean comesIntoPlayTapped(Board board) {
		return true;
	}

	@Override
	public boolean isFetchable(Color color) {
		return false;
	}

}
