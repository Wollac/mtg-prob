package probability.rules;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.HashSet;
import java.util.Set;

import com.google.common.primitives.Chars;

public class StringTokenizer {

  public enum StringTokenType {
    STRING, QUOTED_STRING, OPEN_PARENTHESIS, CLOSE_PARENTHESIS, EOL, EOF, INVALID
  }

  private static final Set<Character> OPERATION_CHARS = getOperationsCharacters();

  private static final int QUOTE_CHAR = '"';

  private final StreamTokenizer _st;

  private String _tokenValue;

  public StringTokenizer(Reader reader) {

    _st = createStreamTokenizer(reader);
  }

  private static StreamTokenizer createStreamTokenizer(Reader reader) {

    StreamTokenizer tokenizer = new StreamTokenizer(reader);

    tokenizer.resetSyntax();

    setVariableChars(tokenizer);
    setNumberChars(tokenizer);
    setOperationChars(tokenizer);
    setSpecialChars(tokenizer);

    tokenizer.slashSlashComments(true);
    tokenizer.eolIsSignificant(true);

    return tokenizer;
  }

  public StringTokenType nextToken() throws IOException {

    int tokenType = _st.nextToken();
    switch (tokenType) {
      case StreamTokenizer.TT_EOF:
      case StreamTokenizer.TT_EOL:
        _tokenValue = "";
        break;
      case StreamTokenizer.TT_WORD:
      case QUOTE_CHAR:
        _tokenValue = _st.sval;
        break;
      case StreamTokenizer.TT_NUMBER:
        _tokenValue = String.valueOf(_st.nval);
        break;
      default:
        _tokenValue = String.valueOf((char) tokenType);
    }

    return code2TokenType(tokenType);
  }

  public int currentLine() {
    return _st.lineno();
  }

  public String tokenValue() {
    return _tokenValue;
  }

  public StringTokenType tokenType() {

    return code2TokenType(_st.ttype);
  }

  private static StringTokenType code2TokenType(int code) {

    switch (code) {
      case StreamTokenizer.TT_EOF:
        return StringTokenType.EOF;
      case StreamTokenizer.TT_EOL:
        return StringTokenType.EOL;
      case StreamTokenizer.TT_WORD:
        return StringTokenType.STRING;
      case QUOTE_CHAR:
        return StringTokenType.QUOTED_STRING;
      case Parentheses.OPEN_PARENTHESIS_CHAR:
        return StringTokenType.OPEN_PARENTHESIS;
      case Parentheses.CLOSE_PARENTHESIS_CHAR:
        return StringTokenType.CLOSE_PARENTHESIS;
      default:
        return StringTokenType.INVALID;
    }
  }


  private static void setVariableChars(StreamTokenizer tokenizer) {
    tokenizer.wordChars('a', 'z');
    tokenizer.wordChars('A', 'Z');
    tokenizer.wordChars('0', '9');
  }

  private static void setNumberChars(StreamTokenizer tokenizer) {
    tokenizer.wordChars('0', '9');
    tokenizer.wordChars('.', '.');
    tokenizer.wordChars('-', '-');
  }

  private static void setSpecialChars(StreamTokenizer tokenizer) {
    tokenizer.whitespaceChars(0, ' ');
    tokenizer.commentChar('#');
    tokenizer.quoteChar(QUOTE_CHAR);
    tokenizer.ordinaryChar(Parentheses.OPEN_PARENTHESIS_CHAR);
    tokenizer.ordinaryChar(Parentheses.CLOSE_PARENTHESIS_CHAR);
  }

  private static void setOperationChars(StreamTokenizer tokenizer) {
    for (char c : OPERATION_CHARS) {
      tokenizer.wordChars(c, c);
    }
  }

  private static Set<Character> getOperationsCharacters() {

    Set<Character> result = new HashSet<>();
    for (Operations op : Operations.values()) {
      result.addAll(Chars.asList(op.getSymbol().toCharArray()));
    }

    return result;
  }

}
