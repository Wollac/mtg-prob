package probability.core.land;

import probability.core.Board;
import probability.core.Colors;

/**
 * Models Slow Fetches such as <a href="http://magiccards.info/query?q=!Flood+Plain">Flood
 * Plain</a>.
 *
 * <p>Slow Fetches are identical to {@linkplain FetchLand} but always always enter the battlefield
 * tapped.
 */
public final class SlowFetchLand extends FetchLand {

  public SlowFetchLand(String name, Colors colors) {
    super(name, colors);
  }

  @Override public boolean comesIntoPlayTapped(Board board) {
    return true;
  }

}
