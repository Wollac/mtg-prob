package probability.csv;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import probability.attr.ImmutableAttributeHolder;
import probability.attr.IntegerAttributeKey;
import probability.attr.ManaCostAttributeKey;
import probability.attr.StringAttributeKey;
import probability.core.Spell;

public class SpellCSVParser extends AbstractCSVParser<Spell> {

	public SpellCSVParser(Reader reader) throws IOException {

		super(reader);

		addOptionalAttribute(ATTR.NUM);
		addMandatoryAttribute(ATTR.NAME);
		addMandatoryAttribute(ATTR.MANA_COST);
	}

	@Override
	protected Collection<Spell> createInstance(ImmutableAttributeHolder row) {

		int num = row.getAttributeValue(ATTR.NUM);

		Collection<Spell> spells = new ArrayList<>(num);

		Spell spell = new Spell(row.getAttributeValue(ATTR.NAME),
				row.getAttributeValue(ATTR.MANA_COST));

		for (int i = 0; i < num; i++) {
			spells.add(spell);
		}

		return spells;
	}

	private static interface ATTR {

		static final IntegerAttributeKey NUM = new IntegerAttributeKey("num", 1,
				i -> (i > 0));

		static final StringAttributeKey NAME = new StringAttributeKey("name");

		static final ManaCostAttributeKey MANA_COST = new ManaCostAttributeKey("cost");
	}

}
