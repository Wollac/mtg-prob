package probability.core.land;

import java.util.Set;

import probability.core.Board;
import probability.core.Card;
import probability.core.Color;

public interface Land extends Card {

    Set<Color> colors();

    boolean comesIntoPlayTapped(Board board);

    Set<Color> producibleColors();

    Set<Color> producesColors(Board board);

}
