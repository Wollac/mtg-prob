package probability.csv;

import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import probability.config.Settings;
import probability.core.Card;
import probability.messages.Messages;
import probability.utils.ReadOrWriteDefaultIO;

abstract class CardsReader<C extends Card> extends ReadOrWriteDefaultIO {

    private static final String FILE_TYPE = "CSV";

    private List<C> _cards = null;

    CardsReader(String filename) {

        super(filename, Settings.CHARSET);
    }

    public List<C> read() {

        try {
            readOrWriteDefault();
        } catch (IOException e) {

            Logger.error(Messages.get().readFileException(getFileName(), FILE_TYPE,
                    e.getLocalizedMessage()));
            Logger.debug(e);
        }

        List<C> result = _cards;
        _cards = null;

        return result;
    }

    @Override
    protected void read(Reader reader) throws IOException {

        AbstractCSVParser<C> parser = createCSVParser(reader);

        try {
            _cards = parser.readAll();
        } catch (AbstractCSVParser.CvsParseException e) {

            Logger.error(Messages.get().parseFileException(getFileName(), FILE_TYPE,
                    e.getLocalizedMessage()));
            Logger.debug(e);
        }
    }

    @Override
    protected void writeDefault(Writer writer) throws IOException {

        Logger.warn(Messages.get().writeDefaultFile(getFileName(), FILE_TYPE));

        AbstractCSVParser<C> parser = createCSVParser();

        String headerLine = parser.getHeaders().stream().collect(Collectors.joining(
                String.valueOf(parser.getSeparator())));

        writer.write(headerLine);
        writer.write(System.lineSeparator());

        _cards = Collections.emptyList();
    }

    private AbstractCSVParser<C> createCSVParser() throws IOException {

        Reader dummyReader = new StringReader("");
        return createCSVParser(dummyReader);
    }

    abstract protected AbstractCSVParser<C> createCSVParser(Reader reader) throws IOException;
}
