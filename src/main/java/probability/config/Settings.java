package probability.config;

import java.io.File;
import java.net.URL;

public class Settings {

    private static final String CONFIG_FILE_NAME = "mtg.config";

    public static final Config config;

    static {
        ConfigLoader loader = new ConfigLoader();

        URL url = Settings.class.getResource(CONFIG_FILE_NAME);
        if (url != null) {
            config = loader.loadFromResource(url);
        } else {
            config = loader.loadFromFileOrWriteDefault(new File(CONFIG_FILE_NAME));
        }
    }
}
