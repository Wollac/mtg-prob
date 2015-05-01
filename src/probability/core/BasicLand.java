package probability.core;

import java.util.Collection;

public class BasicLand extends AbstractLand {

	public BasicLand(String name, Collection<Color> colors) {
		super(name, colors);
	}
	
	public BasicLand(String name, Color... colors) {
		super(name, colors);
	}

	@Override
	public boolean comesIntoPlayTapped() {
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
