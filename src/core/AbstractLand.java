package core;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

abstract class AbstractLand implements Land {

	final private String _name;

	final private Set<Color> _colors;

	public AbstractLand(String name, Color... colors) {
		_name = name;
		_colors = new HashSet<Color>(Arrays.asList(colors));
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public Set<Color> colors() {
		return Collections.unmodifiableSet(_colors);
	}

	@Override
	public boolean canProduce(Color color) {
		if (color == Color.Colorless)
			return true;

		return _colors.contains(color);
	}

}
