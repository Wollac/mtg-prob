package probability.csv;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import probability.attr.ColorsAttribute;
import probability.attr.EnumAttribute;
import probability.attr.ImmutableAttributeHolder;
import probability.attr.IntegerAttribute;
import probability.attr.StringAttribute;
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

		int num = row.getAttributeVale(ATTR.NUM);

		Collection<Land> lands = new ArrayList<>(num);

		Land land = createLand(row.getAttributeVale(ATTR.TYPE),
				row.getAttributeVale(ATTR.NAME),
				row.getAttributeVale(ATTR.COLORS));

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

		static final IntegerAttribute NUM = new IntegerAttribute("num", 1,
				i -> (i > 0));

		static final StringAttribute NAME = new StringAttribute("name");

		static final EnumAttribute<LandTypes> TYPE = new EnumAttribute<>(
				"type", LandTypes.class, LandTypes.Basic);

		static final ColorsAttribute COLORS = new ColorsAttribute("colors");

	}

}
