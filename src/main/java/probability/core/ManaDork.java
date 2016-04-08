package probability.core;

import java.util.Objects;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class ManaDork extends Spell {

    private final Colors _colors;

    public ManaDork(String name, ManaCost cost, Colors colors) {
        super(name, cost);

        _colors = checkNotNull(colors);
    }

    Set<Color> producesColors() {
        return _colors.getColors();
    }

    @Override
    public boolean equals(Object obj) {

        if (super.equals(obj)) {
            ManaDork other = (ManaDork) obj;
            return Objects.equals(_colors, other._colors);
        }

        return false;
    }

}
