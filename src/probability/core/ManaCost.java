package probability.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ManaCost {
	private final Map<Color, Integer> _countMap;

	public ManaCost() {
		_countMap = new HashMap<>();
	}

	public ManaCost(String costString) throws IllegalArgumentException {
		_countMap = new HashMap<>();

		for (char c : costString.toCharArray()) {
			if (Character.isDigit(c)) {
				increaseCount(Color.Colorless, Character.getNumericValue(c));
			} else {
				Color color = Color.getColor(c);

				if (color == null) {
					throw new IllegalArgumentException(c
							+ " is no valid letter code");
				}

				increaseCount(color);
			}
		}
	}

	public ManaCost(ManaCost o) {
		_countMap = new HashMap<>(o._countMap);
	}

	public boolean containsColor(Color color) {
		return getCount(color) > 0 || getCount(Color.Colorless) > 0;
	}

	public void increaseCount(Color color, int inc) {
		if (inc == 0) {
			return;
		}

		_countMap.put(color, getCount(color) + inc);
	}

	public void increaseCount(Color color) {
		increaseCount(color, 1);
	}

	public void decreaseCount(Color color, int dec) {
		if (dec == 0) {
			return;
		}

		int count = getCount(color);

		if (dec > count) {
			throw new IllegalArgumentException("Costs cannot be negative");
		}
		if (dec == count) {
			_countMap.remove(color);
		}

		_countMap.put(color, count - dec);
	}

	public void decreaseCount(Color color) {
		decreaseCount(color, 1);
	}

	public int getCount(Color object) {
		if (_countMap.containsKey(object))
			return _countMap.get(object);
		else
			return 0;
	}

	public Set<Color> getColors() {
		return _countMap.keySet();
	}

	public int getCMC() {
		int total = 0;

		for (int v : _countMap.values()) {
			total += v;
		}

		return total;
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

		return _countMap.equals(other._countMap);
	}

	@Override
	public int hashCode() {
		return _countMap.hashCode();
	}
}
