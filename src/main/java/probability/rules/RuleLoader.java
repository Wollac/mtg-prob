package probability.rules;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import probability.rules.Parentheses.CloseParenthesis;
import probability.rules.Parentheses.OpenParenthesis;
import probability.rules.StringTokenizer.StringTokenType;
import probability.rules.Token.RulesTokenException;
import probability.rules.Value.StringValue;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Class for loading and parsing a rule provided as text input into an evaluable expression.
 */
public class RuleLoader {

    private final VariableHolder _variables;

    public RuleLoader(VariableHolder variables) {
        _variables = checkNotNull(variables);
    }

    private static Expression parseInfixTokens(List<Token> infixExpressions, int lineNumber)
            throws RulesParseException {

        Stack<Token> rpnStack;
        try {
            rpnStack = ShuntingYardAlgorithm.infix2rpn(infixExpressions);
        } catch (RulesTokenException e) {
            throw new RulesParseException(e.getMessage(), lineNumber, e);
        }

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
        } catch (RulesTokenException e) {
            throw new RulesParseException(e.getMessage(), lineNumber, e);
        }

        if (!stack.isEmpty()) {
            throw new RulesParseException(stack.peek() + " is no operand of any operation",
                    lineNumber);
        }

        return expression;
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

        List<Expression> expressions = parse(Objects.requireNonNull(reader));

        return new Rule(expressions);
    }

    private List<Expression> parse(Reader reader) throws IOException, RulesParseException {

        List<Expression> rules = new ArrayList<>();

        StringTokenizer tokenizer = new StringTokenizer(reader);

        for (int lineNumber = 1; tokenizer.tokenType() != StringTokenType.EOF; lineNumber++) {

            List<Token> tokens = tokenizeLine(tokenizer);

            // ignore empty lines
            if (!tokens.isEmpty()) {

                Expression expression = parseInfixTokens(tokens, lineNumber);
                if (expression == null) {
                    throw new RulesParseException("Line could not be parsed",
                            tokenizer.currentLine());
                }

                rules.add(expression);
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

        return new StringValue(tokenValue);
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
