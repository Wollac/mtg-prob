package probability.core.land;

import java.util.Set;

import probability.core.Board;
import probability.core.Card;
import probability.core.Color;

public interface Land extends Card {

	public boolean canProduce(Color color);

	public Set<Color> colors();

	public boolean comesIntoPlayTapped(Board board);

	public boolean isFetchable(Color color);

}
