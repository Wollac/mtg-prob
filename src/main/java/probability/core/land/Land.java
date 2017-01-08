package probability.core.land;

import probability.core.Board;
import probability.core.Card;
import probability.core.Color;

import java.util.Set;

public interface Land extends Card {

  Set<Color> colors();

  boolean comesIntoPlayTapped(Board board);

  Set<Color> producibleColors();

  Iterable<Color> producesColors();

}
