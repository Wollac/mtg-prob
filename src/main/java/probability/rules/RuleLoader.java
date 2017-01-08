package probability.rules;

import probability.rules.engine.Expression;
import probability.rules.engine.Parser;
import probability.rules.parser.StringToken;
import probability.rules.parser.StringTokenizer;
import probability.utils.LineCommentReader;

import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class for loading and parsing a rule provided as text input into an evaluable expression.
 */
public class RuleLoader {

  private final Parser _parser;

  public RuleLoader(VariableHolder variables) {
    _parser = new Parser(variables);
  }

  /**
   * Parses a rule from the given text.
   *
   * @param reader reader representing the characters
   * @return parsed rule
   * @throws IOException         if an error occurred during reading
   * @throws RulesParseException if an error occurred during parsing
   */
  public Rule read(Reader reader) throws IOException, RulesParseException {

    List<Expression> expressions;
    try {
      expressions = parse(Objects.requireNonNull(reader));
    } catch (ParseException e) {
      throw new RulesParseException(e.getMessage(), 0);
    }

    return new Rule(expressions);
  }

  private List<Expression> parse(Reader reader)
      throws IOException, RulesParseException, ParseException {

    List<Expression> rules = new ArrayList<>();

    try (LineCommentReader bufferedReader = new LineCommentReader(reader)) {

      String line;
      while ((line = bufferedReader.readLine()) != null) {

        List<StringToken> stringTokens = StringTokenizer.tokenize(line);

        // ignore empty lines
        if (stringTokens.isEmpty()) {
          continue;
        }

        rules.add(_parser.parseSingleExpression(stringTokens));
      }
    }

    return rules;
  }

  public static class RulesParseException extends Exception {

    private static final long serialVersionUID = 4667863316378356215L;

    private final int _errorLine;

    public RulesParseException(String str, int errorLine) {
      super(str);
      _errorLine = errorLine;
    }

    public RulesParseException(String str, int errorLine, Throwable cause) {
      super(str, cause);
      _errorLine = errorLine;
    }

    public int getErrorLine() {
      return _errorLine;
    }
  }

}
