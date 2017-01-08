package probability.core.land;

import probability.core.Board;
import probability.core.Colors;

/**
 * Models Taplands such as <a href="http://magiccards.info/query?q=!Seaside+Citadel">Seaside
 * Citadel</a>.
 *
 * <p>Taplands always enter the battlefield tapped.
 */
public final class TapLand extends AbstractLand {

  public TapLand(String name, Colors colors) {
    super(name, colors);
  }

  @Override public boolean comesIntoPlayTapped(Board board) {
    return true;
  }

}
