package probability.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Colors {

	private final Set<Color> _colors;

	public Colors(Collection<Color> colors) {
		_colors = new HashSet<>(colors);
	}

	public Colors(Color... colors) {
		this(Arrays.asList(colors));
	}

	private Colors() {
		_colors = new HashSet<>();
	}

	public Colors(Colors colors) {
		this(colors.getColors());
	}

	public Set<Color> getColors() {
		return Collections.unmodifiableSet(_colors);
	}

	public boolean containsColor(Color color) {
		return _colors.contains(color);
	}

	public static Colors valueOf(String str) throws IllegalArgumentException {
		Colors result = new Colors();

		for (int i = 0; i < str.length(); i++) {
			Color color = Color.getColor(str.charAt(i));

			if (result.containsColor(color)) {
				throw new IllegalArgumentException(color + " is contained"
						+ " twice in " + str);
			}

			result._colors.add(color);
		}

		if (result.getColors().isEmpty()) {
			result._colors.add(Color.Colorless);
		}

		return result;
	}

}
