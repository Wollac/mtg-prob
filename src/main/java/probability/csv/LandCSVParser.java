package probability.csv;

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
import probability.core.land.NonbasicLand;
import probability.core.land.SlowFetchLand;
import probability.core.land.TapLand;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

public class LandCSVParser extends AbstractCSVParser<Land> {

  public LandCSVParser(Reader reader) throws IOException {

    super(reader);

    addOptionalAttribute(AttributeKeys.NUM);
    addMandatoryAttribute(AttributeKeys.NAME);
    addMandatoryAttribute(AttributeKeys.TYPE);
    addMandatoryAttribute(AttributeKeys.COLORS);
  }

  private static Land createLand(LandTypes type, String name, Colors colors) {

    switch (type) {
      case Basic:
        return new BasicLand(name, colors);
      case NonBasic:
        return new NonbasicLand(name, colors);
      case Tap:
        return new TapLand(name, colors);
      case Check:
        return new CheckLand(name, colors);
      case Fast:
        return new FastLand(name, colors);
      case Fetch:
        return new FetchLand(name, colors);
      case SlowFetch:
        return new SlowFetchLand(name, colors);
      default:
        throw new AssertionError("unexpected land type " + type);
    }
  }

  @Override protected Collection<Land> createInstance(ImmutableAttributeHolder row) {

    int num = row.getAttributeValue(AttributeKeys.NUM);

    Collection<Land> lands = new ArrayList<>(num);

    Land land = createLand(row.getAttributeValue(AttributeKeys.TYPE), row.getAttributeValue(
        AttributeKeys.NAME),
        row.getAttributeValue(AttributeKeys.COLORS));

    for (int i = 0; i < num; i++) {
      lands.add(land);
    }

    return lands;
  }

  private enum LandTypes {
    Basic, NonBasic, Tap, Check, Fast, Fetch, SlowFetch
  }


  private interface AttributeKeys {

    IntegerAttributeKey NUM = new IntegerAttributeKey("num", 1, i -> (i > 0));

    StringAttributeKey NAME = new StringAttributeKey("name");

    EnumAttributeKey<LandTypes> TYPE =
        new EnumAttributeKey<>("type", LandTypes.class, LandTypes.Basic);

    ColorsAttributeKey COLORS = new ColorsAttributeKey("colors");

  }

}
