package probability.config;

import java.io.File;

public class Settings {

    private static final String CONFIG_FILE_NAME = "mtg.conf";

    public static final Config config;

    static {
        config = new ConfigLoader().load(new File(CONFIG_FILE_NAME));
    }
}
