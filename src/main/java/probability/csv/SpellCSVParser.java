package probability.csv;

import probability.attr.ImmutableAttributeHolder;
import probability.attr.IntegerAttributeKey;
import probability.attr.ManaCostAttributeKey;
import probability.attr.StringAttributeKey;
import probability.core.Spell;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

public class SpellCSVParser extends AbstractCSVParser<Spell> {

  public SpellCSVParser(Reader reader) throws IOException {

    super(reader);

    addOptionalAttribute(AttributeKeys.NUM);
    addMandatoryAttribute(AttributeKeys.NAME);
    addMandatoryAttribute(AttributeKeys.MANA_COST);
  }

  @Override protected Collection<Spell> createInstance(ImmutableAttributeHolder row) {

    int num = row.getAttributeValue(AttributeKeys.NUM);

    Collection<Spell> spells = new ArrayList<>(num);

    Spell spell = new Spell(row.getAttributeValue(AttributeKeys.NAME),
        row.getAttributeValue(AttributeKeys.MANA_COST));

    for (int i = 0; i < num; i++) {
      spells.add(spell);
    }

    return spells;
  }

  private interface AttributeKeys {

    IntegerAttributeKey NUM = new IntegerAttributeKey("num", 1, i -> (i > 0));

    StringAttributeKey NAME = new StringAttributeKey("name");

    ManaCostAttributeKey MANA_COST = new ManaCostAttributeKey("cost");
  }

}
