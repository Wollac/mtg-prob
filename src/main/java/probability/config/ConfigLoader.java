package probability.config;

import com.google.common.io.CharSource;
import com.google.common.io.Resources;

import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.List;

import probability.attr.AttributeKey;
import probability.attr.AttributeParseException;
import probability.attr.AttributeUtils;
import probability.attr.BooleanAttributeKey;
import probability.attr.IntegerAttributeKey;
import probability.attr.PropertiesAttributeHolder;
import probability.messages.Messages;
import probability.utils.ReadOrWriteDefaultIO;

class ConfigLoader {

    private static final String FILE_TYPE = "config";

    private final PropertiesAttributeHolder _configIO;

    ConfigLoader() {

        List<AttributeKey<?>> variableKeys = AttributeUtils.getAttributeKeys(ATTR.class);
        _configIO = new PropertiesAttributeHolder(variableKeys);
    }

    Config loadFromResource(URL url) {

        CharSource source = Resources.asCharSource(url, Settings.CHARSET);
        try {
            _configIO.load(source.openStream());
        } catch (IOException | AttributeParseException e) {
            // the resource file should always be valid
            throw new IllegalStateException("Could not parse resource " + url, e);
        }

        return getConfig();
    }

    Config loadFromFileOrWriteDefault(File configFile) {

        ConfigIO io = new ConfigIO(configFile);

        try {
            io.readOrWriteDefault();
        } catch (IOException e) {
            Logger.error(Messages.get().readFileException(configFile.getName(), FILE_TYPE, e.getLocalizedMessage()));
            Logger.debug(e);
        }

        return getConfig();
    }

    private <T> T getConfigValue(AttributeKey<T> attribute) {

        return _configIO.getAttributeValue(attribute);
    }

    private Config getConfig() {

        return new Config() {
            @Override
            public int numberOfCards() {
                return getConfigValue(ATTR.NUMBER_OF_CARDS);
            }

            @Override
            public int initialHandSize() {
                return getConfigValue(ATTR.INITIAL_HAND_SIZE);
            }

            @Override
            public boolean drawOnTurn() {
                return getConfigValue(ATTR.DRAW_ON_TURN);
            }

            @Override
            public int sampleSize() {
                return getConfigValue(ATTR.SAMPLE_SIZE);
            }

            @Override
            public int turnsAfterMaxCMC() {
                return getConfigValue(ATTR.TURNS_AFTER_MAX_CMC);
            }
        };
    }

    private class ConfigIO extends ReadOrWriteDefaultIO {

        private ConfigIO(File file) {
            super(file, Settings.CHARSET);
        }

        @Override
        protected void read(Reader reader) throws IOException {
            try {
                _configIO.load(reader);
            } catch (AttributeParseException e) {
                Logger.error(Messages.get().parseFileException(getFileName(), FILE_TYPE,
                        e.getLocalizedMessage()));
                Logger.debug(e);
            }
        }

        @Override
        protected void writeDefault(Writer writer) throws IOException {

            Logger.warn(Messages.get().writeDefaultFile(getFileName(), FILE_TYPE));

            _configIO.store(writer, FILE_TYPE);
        }
    }

    private interface ATTR {

        IntegerAttributeKey NUMBER_OF_CARDS = new IntegerAttributeKey(
                "cards", 60, i -> (i > 0));

        IntegerAttributeKey INITIAL_HAND_SIZE = new IntegerAttributeKey(
                "initial_hand_size", 7, i -> (i > 0));

        BooleanAttributeKey DRAW_ON_TURN = new BooleanAttributeKey(
                "draw_on_turn", false);

        IntegerAttributeKey SAMPLE_SIZE = new IntegerAttributeKey(
                "sample_size", 10000, i -> (i >= 1000));

        IntegerAttributeKey TURNS_AFTER_MAX_CMC = new IntegerAttributeKey(
                "turns_after_max_CMC", 3, i -> (i > 0));
    }

}
