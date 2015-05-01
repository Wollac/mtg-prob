package probability.core.land;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import probability.core.Color;
import probability.core.Colors;

abstract class AbstractLand implements Land {

	final private String _name;

	final private HashSet<Color> _colors;

	protected AbstractLand(String name, Colors colors) {
		_name = name;
		_colors = new HashSet<>(colors.getColors());
	}

	@Override
	final public String getName() {
		return _name;
	}

	@Override
	final public Set<Color> colors() {
		return Collections.unmodifiableSet(_colors);
	}

	@Override
	public boolean canProduce(Color color) {
		if (color == Color.Colorless)
			return true;

		return _colors.contains(color);
	}

	@Override
	public String toString() {
		return getName();
	}

}
