package probability.core.land;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import probability.core.Color;
import probability.core.Colors;

abstract class AbstractLand implements Land {

	final private String _name;

	final private HashSet<Color> _colors;

	protected AbstractLand(String name, Colors colors) {
		_name = name;
		_colors = new HashSet<>(colors.getColors());
	}

	@Override
	final public String getName() {
		return _name;
	}

	@Override
	final public CardType getCardType() {
		return CardType.Land;
	}

	@Override
	final public Set<Color> colors() {
		return Collections.unmodifiableSet(_colors);
	}

	@Override
	public final boolean canProduce(Color color) {
		if (color == Color.Colorless)
			return true;

		return producesColors().contains(color);
	}

	@Override
	public Set<Color> producesColors() {
		return colors();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractLand)) {
			return false;
		}

		final AbstractLand other = (AbstractLand) obj;

		return Objects.equals(getClass(), other.getClass())
				&& Objects.equals(_name, other._name)
				&& Objects.equals(_colors, other._colors);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getClass(), _name, _colors);
	}

	@Override
	public String toString() {
		return getName();
	}

}
