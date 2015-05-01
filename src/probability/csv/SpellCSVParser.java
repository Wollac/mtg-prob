package probability.csv;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import probability.attr.ImmutableAttributeHolder;
import probability.attr.IntegerAttribute;
import probability.attr.ManaCostAttribute;
import probability.attr.StringAttribute;
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

		int num = row.getAttributeVale(ATTR.NUM);

		Collection<Spell> spells = new ArrayList<>(num);

		Spell spell = new Spell(row.getAttributeVale(ATTR.NAME),
				row.getAttributeVale(ATTR.MANA_COST));

		for (int i = 0; i < num; i++) {
			spells.add(spell);
		}

		return spells;
	}

	private static interface ATTR {

		static final IntegerAttribute NUM = new IntegerAttribute("num", 1,
				i -> (i > 0));

		static final StringAttribute NAME = new StringAttribute("name");

		static final ManaCostAttribute MANA_COST = new ManaCostAttribute("cost");
	}

}
