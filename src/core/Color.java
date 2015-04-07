package core;

import java.util.HashMap;
import java.util.Map;

public enum Color {
	Colorless('A'), White('W'), Blue('U'), Black('B'), Red('R'), Green('G');

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
		return _codeToColor.get(c);
	}

	public static Color[] allColors() {
		return new Color[] { White, Blue, Black, Red, Green };
	}
}
