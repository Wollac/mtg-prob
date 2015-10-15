package probability.csv;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import probability.attr.ColorsAttributeKey;
import probability.attr.EnumAttributeKey;
import probability.attr.ImmutableAttributeHolder;
import probability.attr.IntegerAttributeKey;
import probability.attr.StringAttributeKey;
import probability.core.Colors;
import probability.core.land.BasicLand;
import probability.core.land.CheckLand;
import probability.core.land.FastLand;
import probability.core.land.FetchLand;
import probability.core.land.Land;
import probability.core.land.NonBasicLand;
import probability.core.land.ReflectingLand;
import probability.core.land.TapLand;

public class LandCSVParser extends AbstractCSVParser<Land> {

	public LandCSVParser(Reader reader) throws IOException {

		super(reader);

		addOptionalAttribute(ATTR.NUM);
		addMandatoryAttribute(ATTR.NAME);
		addMandatoryAttribute(ATTR.TYPE);
		addMandatoryAttribute(ATTR.COLORS);
	}

	@Override
	protected Collection<Land> createInstance(ImmutableAttributeHolder row) {

		int num = row.getAttributeValue(ATTR.NUM);

		Collection<Land> lands = new ArrayList<>(num);

		Land land = createLand(row.getAttributeValue(ATTR.TYPE),
				row.getAttributeValue(ATTR.NAME),
				row.getAttributeValue(ATTR.COLORS));

		for (int i = 0; i < num; i++) {
			lands.add(land);
		}

		return lands;
	}

	private static Land createLand(LandTypes type, String name, Colors colors) {

		switch (type) {
		case Basic:
			return new BasicLand(name, colors);
		case NonBasic:
			return new NonBasicLand(name, colors);
		case Tap:
			return new TapLand(name, colors);
		case Check:
			return new CheckLand(name, colors);
		case Fast:
			return new FastLand(name, colors);
		case Fetch:
			return new FetchLand(name, colors);
		case Reflecting:
			return new ReflectingLand(name, colors);
		default:
			throw new IllegalStateException("unexpected land type " + type);
		}
	}

	private static enum LandTypes {
		Basic, NonBasic, Tap, Check, Fast, Fetch, Reflecting;
	}

	private static interface ATTR {

		static final IntegerAttributeKey NUM = new IntegerAttributeKey("num", 1,
				i -> (i > 0));

		static final StringAttributeKey NAME = new StringAttributeKey("name");

		static final EnumAttributeKey<LandTypes> TYPE = new EnumAttributeKey<>(
				"type", LandTypes.class, LandTypes.Basic);

		static final ColorsAttributeKey COLORS = new ColorsAttributeKey("colors");

	}

}
