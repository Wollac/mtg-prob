package probability.csv;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import probability.attr.ColorsAttributeKey;
import probability.attr.ImmutableAttributeHolder;
import probability.attr.IntegerAttributeKey;
import probability.attr.ManaCostAttributeKey;
import probability.attr.StringAttributeKey;
import probability.core.ManaDork;

public class ManaDorkCSVParser extends AbstractCSVParser<ManaDork> {

  public ManaDorkCSVParser(Reader reader) throws IOException {

    super(reader);

    addOptionalAttribute(ATTR.NUM);
    addMandatoryAttribute(ATTR.NAME);
    addMandatoryAttribute(ATTR.MANA_COST);
    addMandatoryAttribute(ATTR.COLORS);
  }

  @Override
  protected Collection<ManaDork> createInstance(ImmutableAttributeHolder row) {

    int num = row.getAttributeValue(ATTR.NUM);

    Collection<ManaDork> manaDorks = new ArrayList<>(num);

    ManaDork manaDork = new ManaDork(row.getAttributeValue(ATTR.NAME),
        row.getAttributeValue(ATTR.MANA_COST), row.getAttributeValue(ATTR.COLORS));

    for (int i = 0; i < num; i++) {
      manaDorks.add(manaDork);
    }

    return manaDorks;
  }

  private static interface ATTR {

    static final IntegerAttributeKey NUM = new IntegerAttributeKey("num", 1, i -> (i > 0));

    static final StringAttributeKey NAME = new StringAttributeKey("name");

    static final ManaCostAttributeKey MANA_COST = new ManaCostAttributeKey("cost");

    static final ColorsAttributeKey COLORS = new ColorsAttributeKey("colors");
  }

}
