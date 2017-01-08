package probability.csv;

import probability.core.land.Land;

import java.io.IOException;
import java.io.Reader;

public class LandsReader extends CardsReader<Land> {

  public LandsReader(String filename) {
    super(filename);
  }

  @Override protected AbstractCSVParser<Land> createCSVParser(Reader reader) throws IOException {
    return new LandCSVParser(reader);
  }
}
