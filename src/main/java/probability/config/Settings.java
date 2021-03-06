package probability.config;

import com.google.common.base.Charsets;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;

public final class Settings {

  public static final Charset CHARSET = Charsets.UTF_8;

  public static final int LINE_WIDTH = 100;

  public static final String MULLIGAN_RULES_FILE_NAME = "mulligan.txt";

  public static final String LANDS_FILE_NAME = "lands.csv";

  public static final String SPELLS_FILE_NAME = "spells.csv";

  public static final Config config;

  private static final String CONFIG_FILE_NAME = "mtg-probability.properties";

  static {
    ConfigLoader loader = new ConfigLoader();

    URL url = Settings.class.getResource(CONFIG_FILE_NAME);
    if (url != null) {
      Logger.debug("Load config from resources");
      config = loader.loadFromResource(url);
    } else {
      Logger.debug("Load config from file system");
      config = loader.loadFromFileOrWriteDefault(new File(CONFIG_FILE_NAME));
    }
  }

  private Settings() {
    // do not initialize
  }
}
