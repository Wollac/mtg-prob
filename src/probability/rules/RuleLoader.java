package probability.rules;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
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

  public Rule readFromFile(File file) throws IOException, RulesParseException {

    try (Reader reader = Files.newReader(file, Charsets.UTF_8)) {

      List<Expression> expressions = parse(reader);
      return new Rule(expressions);
    }
  }

  public Rule readFromString(String string) throws IOException, RulesParseException {

    try (Reader reader = new StringReader(string)) {

      List<Expression> expressions = parse(reader);
      return new Rule(expressions);
    }
  }

  private List<Expression> parse(Reader reader) throws IOException, RulesParseException {

    List<Expression> rules = new ArrayList<>();

    StringTokenizer tokenizer = new StringTokenizer(reader);

    for (int lineNumber = 1; tokenizer.tokenType() != StringTokenType.EOF; lineNumber++) {
      List<Token> tokens = tokenizeLine(tokenizer);

      // ignore empty lines
      if (!tokens.isEmpty()) {
        rules.add(parseInfixTokens(tokens, lineNumber));
      }
    }

    return rules;
  }

  private List<Token> tokenizeLine(StringTokenizer tokenizer)
      throws IOException, RulesParseException {

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
          throw new RulesParseException("Invalid character " + tokenizer.tokenValue(),
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

  private static Expression parseInfixTokens(List<Token> infixExpressions, int lineNumber)
      throws RulesParseException {

    Stack<Token> rpnStack = ShuntingYardAlgorithm.infix2rpn(infixExpressions);
    return parseRPNStack(rpnStack, lineNumber);
  }

  private static Expression parseRPNStack(Stack<Token> stack, int lineNumber)
      throws RulesParseException {

    if (stack.isEmpty()) {
      return null;
    }

    Expression expression;
    try {
      expression = stack.pop().parse(stack);
    } catch (AttributeParseException e) {
      throw new RulesParseException("Value could not be parsed", lineNumber, e);
    }

    if (!stack.isEmpty()) {
      throw new RulesParseException(stack.peek() + " is no opperant of any operation", lineNumber);
    }

    return expression;
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
