package probability.rules.engine;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import probability.rules.RuleLoader;
import probability.rules.VariableHolder;
import probability.rules.engine.Parentheses.CloseParenthesis;
import probability.rules.engine.Parentheses.OpenParenthesis;
import probability.rules.parser.StringToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {

  private final VariableHolder _variables;

  public Parser(VariableHolder variables) {
    _variables = checkNotNull(variables);
  }

  public Expression parseSingleExpression(List<StringToken> stringTokens)
      throws RuleLoader.RulesParseException {

    checkNotNull(stringTokens);
    checkArgument(stringTokens.size() >= 1);

    List<Token> ruleTokens = new ArrayList<>();

    for (StringToken stringToken : stringTokens) {
      switch (stringToken.getType()) {

        case OPEN_PARENTHESIS:
          ruleTokens.add(OpenParenthesis.INSTANCE);
          break;
        case CLOSE_PARENTHESIS:
          ruleTokens.add(CloseParenthesis.INSTANCE);
          break;
        case QUOTED_STRING:
          ruleTokens.add(new Value.StringValue(stringToken.getString()));
          break;
        case STRING:
          ruleTokens.add(string2Token(stringToken.getString()));
          break;
        default:
          throw new IllegalArgumentException();
      }
    }

    return parseInfixTokens(ruleTokens);
  }

  /**
   * Returns an instance of the operator corresponding to the string, or returns a StringValue if
   * no such operator exists.
   */
  private Token string2Token(String tokenValue) {

    Operation op = Operation.getOperationFromSymbol(tokenValue);
    if (op != null) {
      return op.newInstance();
    }

    if (_variables.isRegistered(tokenValue)) {
      return _variables.getVariable(tokenValue);
    }

    return new Value.StringValue(tokenValue);
  }

  private static Expression parseInfixTokens(List<Token> infixExpressions)
      throws RuleLoader.RulesParseException {

    Stack<Token> rpnStack;
    try {
      rpnStack = ShuntingYardAlgorithm.infix2rpn(infixExpressions);
    } catch (Token.RulesTokenException e) {
      throw new RuleLoader.RulesParseException(e.getMessage(), 0, e);
    }

    return parseRPNStack(rpnStack);
  }

  private static Expression parseRPNStack(Stack<Token> stack)
      throws RuleLoader.RulesParseException {

    if (stack.isEmpty()) {
      return null;
    }

    Expression expression;
    try {
      expression = stack.pop().parse(stack);
    } catch (Token.RulesTokenException e) {
      throw new RuleLoader.RulesParseException(e.getMessage(), 0, e);
    }

    if (!stack.isEmpty()) {
      throw new RuleLoader.RulesParseException(stack.peek() + " is no operand of any operation", 0);
    }

    return expression;
  }
}
