package probability.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

abstract class AbstractLand implements Land {

	final private String _name;

	final private Set<Color> _colors;

	protected AbstractLand(String name, Collection<Color> colors) {
		_name = name;
		_colors = new HashSet<Color>(colors);
	}

	protected AbstractLand(String name, Color... colors) {
		this(name, Arrays.asList(colors));
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