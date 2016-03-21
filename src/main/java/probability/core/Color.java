package probability.core;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Color {
    White('W'), Blue('U'), Black('B'), Red('R'), Green('G'), Colorless('C');

    private static final Map<Character, Color> _codeToColor;

    static {
        _codeToColor = new HashMap<>();

        for (Color c : Color.values()) {
            _codeToColor.put(c.getLetterCode(), c);
        }
    }

    private final char _c;

    Color(char c) {
        _c = c;
    }

    public static Color getColor(char c) {

        if (!_codeToColor.containsKey(c)) {
            throw new IllegalArgumentException("illegal character '" + c
                    + "' is not a valid mana letter " + _codeToColor.keySet());
        }

        return _codeToColor.get(c);
    }

    public static EnumSet<Color> emptyEnumSet() {
        return EnumSet.noneOf(Color.class);
    }

    public static int numberOfColors() {
        return _codeToColor.size();
    }

    public char getLetterCode() {
        return _c;
    }
}
