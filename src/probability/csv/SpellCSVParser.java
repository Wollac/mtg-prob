package probability.csv;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import probability.core.Spell;

public class SpellCSVParser extends AbstractCSVParser<Spell> {

	public SpellCSVParser(Reader reader) throws IOException {

		super(reader);

		addAttribute(ATTR.NUM);
		addAttribute(ATTR.NAME);
		addAttribute(ATTR.MANA_COST);
	}

	@Override
	protected Collection<Spell> createInstance(Row row) {

		int num = row.getAttributeVale(ATTR.NUM);

		Collection<Spell> spells = new ArrayList<>(num);

		Spell spell = new Spell(row.getAttributeVale(ATTR.NAME),
				row.getAttributeVale(ATTR.MANA_COST));

		for (int i = 0; i < num; i++) {
			spells.add(spell);
		}

		return spells;
	}

	private static final class ATTR {

		private static final IntegerAttribute NUM = new IntegerAttribute("num",
				1);

		private static final StringAttribute NAME = new StringAttribute("name");

		private static final ManaCostAttribute MANA_COST = new ManaCostAttribute(
				"cost");
	}

}
