package probability.core.land;


import probability.core.Board;
import probability.core.Colors;

public class CheckLand extends AbstractLand {

    public CheckLand(String name, Colors colors) {
        super(name, colors);
    }

    @Override
    public boolean comesIntoPlayTapped(Board board) {
        return !board.isBasicColorPlayed(colors());
    }

}
