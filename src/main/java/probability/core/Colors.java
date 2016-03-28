package probability.core;

import com.google.common.collect.ForwardingSet;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public final class Colors extends ForwardingSet<Color> implements Set<Color> {

    private final Set<Color> _colors;

    private volatile int _hashCode;

    public Colors(EnumSet<Color> colors) {
        _colors = Collections.unmodifiableSet(colors.clone());
    }

    public Colors(Iterable<Color> colors) {

        EnumSet<Color> colorSet = Color.emptyEnumSet();

        for (Color color : colors) {
            colorSet.add(color);
        }

        _colors = Collections.unmodifiableSet(colorSet);
    }

    public Colors(Color... colors) {
        this(Arrays.asList(colors));
    }

    public Colors(Colors colors) {
        this(colors.getColors());
    }

    public static Colors valueOf(String str) throws IllegalArgumentException {

        EnumSet<Color> colorSet = Color.emptyEnumSet();

        for (int i = 0; i < str.length(); i++) {

            Color color = Color.getColor(str.charAt(i));

            if (colorSet.contains(color)) {
                throw new IllegalArgumentException(color + " is contained twice in " + str);
            }

            colorSet.add(color);
        }

        if (colorSet.isEmpty()) {
            colorSet.add(Color.Colorless);
        }

        return new Colors(colorSet);
    }

    public Set<Color> getColors() {
        return _colors;
    }

    @Override
    protected Set<Color> delegate() {
        return _colors;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Colors)) {
            return false;
        }

        Colors other = (Colors) obj;
        return Objects.equals(_colors, other._colors);
    }

    @Override
    public int hashCode() {
        int result = _hashCode;

        if (result == 0) {
            result = _colors.hashCode();
            _hashCode = result;
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Color color : _colors) {
            sb.append(color.getLetterCode());
        }

        return sb.toString();
    }

}
