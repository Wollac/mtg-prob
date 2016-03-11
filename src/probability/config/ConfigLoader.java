package probability.config;

import java.io.File;
import java.io.IOException;

import probability.attr.AttributeKey;
import probability.attr.BooleanAttributeKey;
import probability.attr.IntegerAttributeKey;

public class ConfigLoader {

    private final GenericJsonIO configIO;

    public ConfigLoader() {

        configIO = new GenericJsonIO(ATTR.NUMBER_OF_CARDS, ATTR.INITIAL_HAND_SIZE, ATTR.DRAW_ON_TURN, ATTR.SAMPLE_SIZE);
    }

    public Config load(File configFile) {

        if (!configFile.exists()) {
            writeDefaultConfigFile(configFile);
        } else {
            loadConfigFile(configFile);
        }

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
        };
    }

    private void writeDefaultConfigFile(File configFile) {

        System.err.println("Config file " + configFile + " does not exist," +
                " creating new file with default values");
        try {
            configIO.writeDefaultValues(configFile);
        } catch (IOException e) {
            System.err.println("Could not create, using defaults: " + e.getMessage());
        }
    }


    private void loadConfigFile(File configFile) {

        try {
            configIO.load(configFile);
        } catch (IOException | GenericJsonIO.JsonIOException e) {
            System.err.println("Could not parse config file, using defaults: " + e.getMessage());
        }
    }

    private <T> T getConfigValue(AttributeKey<T> attribute) {

        return configIO.getProperty(attribute);
    }

    private interface ATTR {

        IntegerAttributeKey NUMBER_OF_CARDS = new IntegerAttributeKey(
                "cards", 60, i -> (i > 0));

        IntegerAttributeKey INITIAL_HAND_SIZE = new IntegerAttributeKey(
                "initial hand size", 7, i -> (i > 0));

        BooleanAttributeKey DRAW_ON_TURN = new BooleanAttributeKey(
                "draw on turn", false);

        IntegerAttributeKey SAMPLE_SIZE = new IntegerAttributeKey(
                "sample size", 10000, i -> (i >= 1000));

    }

}
