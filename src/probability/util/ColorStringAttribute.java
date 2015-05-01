package probability.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import probability.core.Color;

public class ColorStringAttribute extends Attribute<Set> {

	public ColorStringAttribute(String name) {
		this(name, s -> true);
	}

	public ColorStringAttribute(String name, Predicate<Set> validator) {
		super(name, Set.class, Collections.emptySet(), validator);
	}

	@Override
	public Set parseValue(String valueString) throws AttributeParseException {

		Set<Color> result = new HashSet<>();

		try {
			for (char c : valueString.toCharArray()) {
				Color color = Color.getColor(c);

				if (result.contains(color)) {
					throw new AttributeParseException(color + " is contained"
							+ " twice in " + valueString, this);
				}

				result.add(color);
			}
		} catch (IllegalArgumentException e) {
			throw new AttributeParseException(valueString
					+ " is not a valid value string", this);
		}

		return result;
	}
}
