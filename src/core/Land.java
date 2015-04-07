package core;

import java.util.Set;

public interface Land extends Card {

	public boolean canProduce(Color color);

	public Set<Color> colors();

	public boolean comesIntoPlayTapped();

	public boolean isFetchable(Color color);

}
