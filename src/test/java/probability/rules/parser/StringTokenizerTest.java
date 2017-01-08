package probability.rules.parser;

import com.google.common.base.Strings;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StringTokenizerTest {

  private static final String[] STRINGS = {"STRING0", "STRING1", "STRING2", "STRING3", "STRING4"};

  @Test public void testEmptyString() throws ParseException {
    List<StringToken> tokens = tokenize("");

    assertStringsEqual(tokens, Collections.emptyList());
  }

  @Test public void testSingleString() throws ParseException {
    String input = STRINGS[0];
    List<StringToken> tokens = tokenize(input);

    assertEqualsStringSplit(tokens, input);
  }

  @Test public void testMultipleIdenticalStrings() throws ParseException {
    String input = Strings.repeat(STRINGS[0] + " ", 4);
    List<StringToken> tokens = tokenize(input);

    assertEqualsStringSplit(tokens, input);
  }

  @Test public void testMultipleStrings() throws ParseException {
    List<StringToken> tokens =
        tokenize(STRINGS[0] + ' ' + STRINGS[1] + "  " + STRINGS[2] + "\t" + STRINGS[3] + " ");

    assertStringsEqual(tokens, Arrays.copyOf(STRINGS, 4));
  }

  @Test public void testQuotedString() throws ParseException {
    final String joinedStrings = joinStrings(4);
    List<StringToken> tokens =
        tokenize(StringTokenizer.QUOTE_CHAR + joinedStrings + StringTokenizer.QUOTE_CHAR);

    assertStringsEqual(tokens, joinedStrings);
  }

  @Test public void testQuotedStringWithoutSpaces() throws ParseException {
    List<StringToken> tokens =
        tokenize(STRINGS[0] + StringTokenizer.QUOTE_CHAR + STRINGS[1] + StringTokenizer.QUOTE_CHAR);

    assertStringsEqual(tokens, Arrays.copyOf(STRINGS, 2));
  }

  @Test(expected = ParseException.class) public void testNotMatchingQuotation()
      throws ParseException {
    List<StringToken> tokens = tokenize("\"STRING");

    assertStringsEqual(tokens, "STRING");
  }

  @Test public void testOpenParenthesis() throws ParseException {
    List<StringToken> tokens = tokenize("(");

    Assert.assertNotNull(tokens);
    Assert.assertEquals(1, tokens.size());

    StringToken expectedToken = new StringToken(StringToken.TokenType.OPEN_PARENTHESIS, "(", 0);
    Assert.assertEquals(tokens.get(0), expectedToken);
  }

  @Test public void testOpenParenthesisBetweenStrings() throws ParseException {
    List<StringToken> tokens = tokenize(STRINGS[0] + "(" + STRINGS[1]);

    Assert.assertNotNull(tokens);
    Assert.assertEquals(3, tokens.size());

    StringToken expectedToken =
        new StringToken(StringToken.TokenType.OPEN_PARENTHESIS, "(", STRINGS[0].length());
    Assert.assertEquals(tokens.get(1), expectedToken);
  }

  @Test public void testCloseParenthesis() throws ParseException {
    List<StringToken> tokens = tokenize(")");

    Assert.assertNotNull(tokens);
    Assert.assertEquals(1, tokens.size());

    StringToken expectedToken = new StringToken(StringToken.TokenType.CLOSE_PARENTHESIS, ")", 0);
    Assert.assertEquals(tokens.get(0), expectedToken);
  }

  @Test public void testCloseParenthesisBetweenStrings() throws ParseException {
    List<StringToken> tokens = tokenize(STRINGS[0] + ")" + STRINGS[1]);

    Assert.assertNotNull(tokens);
    Assert.assertEquals(3, tokens.size());

    StringToken expectedToken =
        new StringToken(StringToken.TokenType.CLOSE_PARENTHESIS, ")", STRINGS[0].length());
    Assert.assertEquals(tokens.get(1), expectedToken);
  }

  @Test public void testStringInParenthesis() throws ParseException {
    List<StringToken> tokens = tokenize("(" + STRINGS[0] + ")");

    StringToken expectedTokens[] = {new StringToken(StringToken.TokenType.OPEN_PARENTHESIS, "(", 0),
        new StringToken(StringToken.TokenType.STRING, STRINGS[0], 1),
        new StringToken(StringToken.TokenType.CLOSE_PARENTHESIS, ")", STRINGS[0].length() + 1)};

    assertTokensEqual(tokens, expectedTokens);
  }

  private static void assertTokensEqual(List<? extends StringToken> actual,
      StringToken[] expected) {

    Assert.assertNotNull(actual);
    Assert.assertEquals(expected.length, actual.size());

    for (int i = 0; i < expected.length; i++) {
      Assert.assertEquals("token " + i, expected[i], actual.get(i));
    }
  }

  private static List<StringToken> tokenize(String string) throws ParseException {
    return StringTokenizer.tokenize(string);
  }

  private static String joinStrings(int maxsize) {
    return Arrays.stream(STRINGS).limit(maxsize).collect(Collectors.joining(" "));
  }

  private static void assertStringsEqual(List<? extends StringToken> actual, String... expected) {
    assertStringsEqual(actual, Arrays.asList(expected));
  }

  private static void assertStringsEqual(List<? extends StringToken> actual,
      List<String> expected) {
    ArrayList<String> actualStrings = actual.stream().map(StringToken::getString)
        .collect(Collectors.toCollection(ArrayList::new));

    Assert.assertNotNull(actual);
    Assert.assertEquals(new ArrayList<>(expected), actualStrings);
  }

  private static void assertEqualsStringSplit(List<? extends StringToken> actual, String input) {

    String[] expectedStrings = input.split("\\s");

    assertStringsEqual(actual, expectedStrings);

    int fromIndex = 0;
    for (StringToken token : actual) {

      Assert.assertEquals(StringToken.TokenType.STRING, token.getType());

      int pos = input.indexOf(token.getString(), fromIndex);
      Assert.assertEquals(pos, token.getPos());
      fromIndex = pos + 1;
    }

  }

}
