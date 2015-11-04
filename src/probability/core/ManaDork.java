package probability.core;

import java.util.Set;

public class ManaDork extends Spell {

  private final Colors _colors;

  public ManaDork(String name, ManaCost cost, Colors colors) {
    super(name, cost);

    _colors = colors;
  }

  Set<Color> producesColors(Board board) {
    return _colors.getColors();
  }

}
