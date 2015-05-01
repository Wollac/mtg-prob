package probability.core.land;

import probability.core.Board;
import probability.core.Colors;

public class FastLand extends NonBasicLand {

	public FastLand(String name, Colors colors) {
		super(name, colors);
	}

	@Override
	public boolean comesIntoPlayTapped(Board board) {
		return board.getNumPlayedLands() > 2;
	}

}
