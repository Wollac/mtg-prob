package probability.rules.parser;

import static probability.rules.parser.StringToken.TokenType;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class StringTokenizer {

  /**
   * Character starting and ending a quotation.
   */
  public static final char QUOTE_CHAR = '"';

  /**
   * Parses the string and returns the respective string tokens.
   */
  public static List<StringToken> tokenize(String string) throws ParseException {

    List<StringToken> result = new ArrayList<>();

    StringStack buffer = new StringStack(string);

    while (true) {

      stripStart(buffer);
      if (buffer.length() == 0) {
        break;
      }

      final int pos = string.length() - buffer.length();

      StringToken token = null;
      for (TokenType type : TokenType.values()) {

        Matcher matcher = type.match(buffer);
        if (matcher.find()) {

          String tokenString = popToken(matcher, buffer);
          token = new StringToken(type, tokenString, pos);
          break;
        }
      }

      if (token != null) {
        result.add(token);
      } else {
        throw new ParseException("Could not parse token", pos);
      }
    }

    return result;
  }

  private StringTokenizer() {
  }

  /**
   * Returns the value string of the matched token and removes it from the buffer.
   */
  private static String popToken(Matcher matcher, StringStack buffer) {

    int group = matcher.groupCount();

    String token = matcher.group(group);
    buffer.pop(matcher.end(1));

    return token;
  }

  /**
   * Removes all leading white space characters from the buffer.
   */
  private static void stripStart(StringStack buffer) {

    int start = 0;

    final int strLen = buffer.length();
    while (start < strLen && Character.isWhitespace(buffer.charAt(start))) {
      ++start;
    }
    buffer.pop(start);
  }

}
