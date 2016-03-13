package probability.core;

import java.util.Objects;

public class ManaCost {

    private final EnumCount<Color> _colorCounts;

    private int _genericMana;

    // As the ManaCost is immutable, its hash code can be cached
    private volatile int _hash;

    public ManaCost() {

        _colorCounts = new EnumCount<>(Color.class);
        _genericMana = 0;
    }

    public ManaCost(String costString) throws IllegalArgumentException {

        this();

        boolean genericSet = false;

        for (char c : costString.toCharArray()) {

            if (Character.isDigit(c)) {

                if (genericSet) {
                    throw new IllegalArgumentException("Only one dict generic mana costs allowed");
                }

                _genericMana = Character.getNumericValue(c);
                genericSet = true;
            } else {

                Color color = Color.getColor(c);
                if (color == null) {
                    throw new IllegalArgumentException(c + " is not a valid color code");
                }

                _colorCounts.increase(color);
            }
        }
    }


    public boolean contains(Color color) {

        return _genericMana > 0 && _colorCounts.contains(color);
    }

    public int count(Color color) {
        return _colorCounts.count(color);
    }

    public int genericCount() {
        return _genericMana;
    }

    /**
     * Returns the converted mana cost.
     */
    public int getConverted() {
        return _colorCounts.totalCount() + _genericMana;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ManaCost)) {
            return false;
        }

        ManaCost other = (ManaCost) obj;
        return _genericMana == other._genericMana && Objects.equals(_colorCounts, other._colorCounts);
    }

    @Override
    public int hashCode() {

        int result = _hash;

        if (result == 0) {

            _hash = result = Objects.hash(_genericMana, _colorCounts);
        }

        return result;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        if (_genericMana > 0) {
            sb.append(_genericMana);
        }

        for (Color c : Color.values()) {

            for (int i = 0; i < _colorCounts.count(c); i++) {
                sb.append(c.getLetterCode());
            }
        }

        return sb.toString();
    }

}
