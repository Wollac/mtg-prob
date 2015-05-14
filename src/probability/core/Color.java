package probability.core;

import java.util.HashMap;
import java.util.Map;

public enum Color {
	Colorless('A'), White('W'), Blue('U'), Black('B'), Red('R'), Green('G');

	private static Color[] ALL_COLORS = { White, Blue, Black, Red, Green };

	private final char _c;

	Color(char c) {
		_c = c;
	}

	public char getLetterCode() {
		return _c;
	}

	private static final Map<Character, Color> _codeToColor;

	static {
		_codeToColor = new HashMap<>();

		for (Color c : Color.values()) {
			_codeToColor.put(c.getLetterCode(), c);
		}
	}

	public static Color getColor(char c) {
		if (!_codeToColor.containsKey(c)) {
			throw new IllegalArgumentException(c + " is not a valid color code");
		}

		return _codeToColor.get(c);
	}

	public static int numberOfColors() {
		return _codeToColor.size();
	}

	public static Color[] allColors() {
		return ALL_COLORS;
	}
}
