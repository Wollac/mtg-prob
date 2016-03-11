package probability.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Colors {

	private final Set<Color> _colors;

	private volatile int _hashCode;

	public Colors(Collection<Color> colors) {
		_colors = Collections.unmodifiableSet(new HashSet<>(colors));
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
		return _colors;
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

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Colors)) {
			return false;
		}

		Colors other = (Colors) obj;
		return Objects.equals(_colors, other._colors);
	}

	@Override
	public int hashCode() {
		int result = _hashCode;

		if (result == 0) {
			result = _colors.hashCode();
			_hashCode = result;
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Color color : _colors) {
			sb.append(color.getLetterCode());
		}

		return sb.toString();
	}

}
