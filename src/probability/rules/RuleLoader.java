package probability.rules;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import probability.attr.AttributeKey.AttributeParseException;
import probability.rules.Parentheses.CloseParenthesis;
import probability.rules.Parentheses.OpenParenthesis;
import probability.rules.StringTokenizer.StringTokenType;
import probability.rules.Value.StringValue;

public class RuleLoader {

  private VariableHolder _variables;

  public RuleLoader(VariableHolder variables) {
    _variables = variables;
  }

  public Rule readFromFile(File file) throws IOException, ParseException, AttributeParseException {

    try (Reader reader = Files.newReader(file, Charsets.UTF_8)) {

      List<Expression> expressions = parse(reader);
      return new Rule(expressions);
    }
  }

  public Rule readFromString(String string)
      throws IOException, ParseException, AttributeParseException {

    try (Reader reader = new StringReader(string)) {

      List<Expression> expressions = parse(reader);
      return new Rule(expressions);
    }
  }

  private List<Expression> parse(Reader reader)
      throws IOException, ParseException, AttributeParseException {

    List<Expression> rules = new ArrayList<>();

    StringTokenizer tokenizer = new StringTokenizer(reader);

    while (tokenizer.tokenType() != StringTokenType.EOF) {
      List<Token> tokens = tokenizeLine(tokenizer);

      // ignore empty lines
      if (!tokens.isEmpty()) {
        rules.add(parseInfixTokens(tokens));
      }
    }

    return rules;
  }

  private List<Token> tokenizeLine(StringTokenizer tokenizer) throws IOException, ParseException {

    List<Token> lineToken = new ArrayList<>();

    // read tokens until EOL or EOF
    while (true) {
      tokenizer.nextToken();
      switch (tokenizer.tokenType()) {
        case EOF:
        case EOL:
          return lineToken;
        case STRING:
          lineToken.add(string2Token(tokenizer.tokenValue()));
          break;
        case QUOTED_STRING:
          lineToken.add(new StringValue(tokenizer.tokenValue()));
          break;
        case OPEN_PARENTHESIS:
          lineToken.add(OpenParenthesis.INSTANCE);
          break;
        case CLOSE_PARENTHESIS:
          lineToken.add(CloseParenthesis.INSTANCE);
          break;
        case INVALID:
          throw new ParseException("Parsing Error: unrecognized token: " + tokenizer.tokenValue(),
              tokenizer.currentLine());
      }
    }
  }

  private Token string2Token(String tokenValue) {

    Operations op = Operations.getOperation(tokenValue);
    if (op != null) {
      return op.getInstance();
    }

    if (_variables.isRegistered(tokenValue)) {
      return _variables.getVariable(tokenValue);
    }

    return new StringValue(tokenValue);
  }

  private static Expression parseInfixTokens(List<Token> infixExpressions)
      throws AttributeParseException {

    Stack<Token> rpnStack = ShuntingYardAlgorithm.infix2rpn(infixExpressions);
    return parseRPNStack(rpnStack);
  }

  private static Expression parseRPNStack(Stack<Token> stack) throws AttributeParseException {

    if (stack.isEmpty()) {
      return null;
    }

    Expression expression = stack.pop().parse(stack);

    if (!stack.isEmpty()) {
      // TODO: throw proper exception
      throw new IllegalArgumentException();
    }

    return expression;
  }

}
