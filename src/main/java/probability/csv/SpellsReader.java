package probability.csv;

import probability.core.Spell;

import java.io.IOException;
import java.io.Reader;

public class SpellsReader extends CardsReader<Spell> {

    public SpellsReader(String filename) {
        super(filename);
    }

    @Override
    protected AbstractCSVParser<Spell> createCSVParser(Reader reader) throws IOException {
        return new SpellCSVParser(reader);
    }
}
