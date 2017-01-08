package probability.csv;

import probability.attr.ColorsAttributeKey;
import probability.attr.ImmutableAttributeHolder;
import probability.attr.IntegerAttributeKey;
import probability.attr.ManaCostAttributeKey;
import probability.attr.StringAttributeKey;
import probability.core.ManaDork;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

public class ManaDorkCSVParser extends AbstractCSVParser<ManaDork> {

  public ManaDorkCSVParser(Reader reader) throws IOException {

    super(reader);

    addOptionalAttribute(AttributeKeys.NUM);
    addMandatoryAttribute(AttributeKeys.NAME);
    addMandatoryAttribute(AttributeKeys.MANA_COST);
    addMandatoryAttribute(AttributeKeys.COLORS);
  }

  @Override protected Collection<ManaDork> createInstance(ImmutableAttributeHolder row) {

    int num = row.getAttributeValue(AttributeKeys.NUM);

    Collection<ManaDork> manaDorks = new ArrayList<>(num);

    ManaDork manaDork = new ManaDork(row.getAttributeValue(AttributeKeys.NAME),
        row.getAttributeValue(AttributeKeys.MANA_COST),
        row.getAttributeValue(AttributeKeys.COLORS));

    for (int i = 0; i < num; i++) {
      manaDorks.add(manaDork);
    }

    return manaDorks;
  }

  private interface AttributeKeys {

    IntegerAttributeKey NUM = new IntegerAttributeKey("num", 1, i -> (i > 0));

    StringAttributeKey NAME = new StringAttributeKey("name");

    ManaCostAttributeKey MANA_COST = new ManaCostAttributeKey("cost");

    ColorsAttributeKey COLORS = new ColorsAttributeKey("colors");
  }

}
