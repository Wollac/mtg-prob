package probability.core;

import java.util.Collections;
import java.util.Set;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class ManaCost {
	private final Multiset<Color> _colorCounts;

	public ManaCost() {
		_colorCounts = HashMultiset.create();
	}

	public ManaCost(String costString) throws IllegalArgumentException {
		_colorCounts = HashMultiset.create();

		for (char c : costString.toCharArray()) {
			if (Character.isDigit(c)) {
				addColor(Color.Colorless, Character.getNumericValue(c));
			} else {
				Color color = Color.getColor(c);

				if (color == null) {
					throw new IllegalArgumentException(c
							+ " is no valid letter code");
				}

				addColor(color);
			}
		}
	}

	public ManaCost(ManaCost o) {
		_colorCounts = HashMultiset.create(o._colorCounts);
	}

	public boolean containsColor(Color color) {
		return getCount(color) > 0 || getCount(Color.Colorless) > 0;
	}

	public void addColor(Color color, int inc) {
		_colorCounts.add(color, inc);
	}

	public void addColor(Color color) {
		_colorCounts.add(color);
	}

	public void removeColor(Color color, int dec) {
		_colorCounts.remove(color, dec);
	}

	public void removeColor(Color color) {
		_colorCounts.remove(color);
	}

	public int getCount(Color color) {
		return _colorCounts.count(color);
	}

	public Set<Color> getColors() {
		return Collections.unmodifiableSet(_colorCounts.elementSet());
	}

	public int getCMC() {
		return _colorCounts.size();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Color c : Color.values()) {
			if (c == Color.Colorless) {
				int colorless = getCount(Color.Colorless);

				if (colorless > 0) {
					sb.append(colorless);
				}
			} else {
				for (int i = 0; i < getCount(c); i++) {
					sb.append(c.getLetterCode());
				}
			}

		}

		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ManaCost)) {
			return false;
		}

		final ManaCost other = (ManaCost) obj;

		return _colorCounts.equals(other._colorCounts);
	}

	@Override
	public int hashCode() {
		return _colorCounts.hashCode();
	}
}
