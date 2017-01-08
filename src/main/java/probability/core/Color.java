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

  private final char _letterCode;

  Color(char letterCode) {
    _letterCode = letterCode;
  }

  /**
   * Returns the Color object of the given character representing the color.
   *
   * @param letterCode the single character representing a color
   * @throws IllegalArgumentException if the character represents no color
   */
  public static Color getColor(char letterCode) {

    Color color = _codeToColor.get(letterCode);
    if (color == null) {
      throw new IllegalArgumentException(
          "illegal character '" + letterCode + "' is not a valid mana letter " + _codeToColor
              .keySet());
    }

    return color;
  }

  public static EnumSet<Color> emptyEnumSet() {
    return EnumSet.noneOf(Color.class);
  }

  public char getLetterCode() {
    return _letterCode;
  }

  @Override public String toString() {
    return String.valueOf(getLetterCode());
  }
}
