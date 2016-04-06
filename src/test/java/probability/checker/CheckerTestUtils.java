package probability.checker;

import probability.core.Color;
import probability.core.Colors;
import probability.core.land.BasicLand;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

final class CheckerTestUtils {

    private CheckerTestUtils() {
        // do not initialize
    }

    static BasicLand createBasicLand(Color... colors) {
        Colors landColors = new Colors(colors);
        return new BasicLand("BASIC-" + landColors.toString(), landColors);
    }

    static Set<Color> getDifferentColors(Color first, Color... rest) {
        return EnumSet.complementOf(EnumSet.of(first, rest));
    }

    static Color getDifferentColor(Color first, Color... rest) {

        Collection<Color> differentColors = getDifferentColors(first, rest);

        if (differentColors.isEmpty()) {
            return null;
        }
        return differentColors.iterator().next();
    }
}
