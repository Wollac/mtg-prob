package probability.core;

import java.util.Collection;

public class NonBasicLand extends AbstractLand implements Land {

	public NonBasicLand(String name, Collection<Color> colors) {
		super(name, colors);
	}

	public NonBasicLand(String name, Color... colors) {
		super(name, colors);
	}

	@Override
	public boolean comesIntoPlayTapped() {
		return true;
	}

	@Override
	public boolean isFetchable(Color color) {
		return false;
	}

}
