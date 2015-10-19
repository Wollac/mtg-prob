package probability.rules;

import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;
import probability.attr.ImmutableAttributeHolder;

interface Parentheses extends Operator {

  static final char OPEN_PARENTHESIS_CHAR = '(';
  static final char CLOSE_PARENTHESIS_CHAR = ')';

  static final int PARENTHESIS_PRECEDENCE = -1;

  enum OpenParenthesis implements Parentheses {
    INSTANCE;

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {
      // TODO: throw correct excpetion
      return false;
    }

    @Override
    public void parse(Stack<Expression> stack) throws AttributeParseException {}

    @Override
    public Operator getInstance() {
      return INSTANCE;
    }

    @Override
    public String getSymbol() {
      return String.valueOf(OPEN_PARENTHESIS_CHAR);
    }

    @Override
    public int getPrecedence() {
      return PARENTHESIS_PRECEDENCE;
    }

    @Override
    public ExpressionType getExpressionType() {
      return ExpressionType.OPEN_PARENTHESIS;
    }

  }

  enum CloseParenthesis implements Parentheses {
    INSTANCE;

    @Override
    public boolean interpret(ImmutableAttributeHolder bindings) {
      // TODO: throw correct excpetion
      return false;
    }

    @Override
    public void parse(Stack<Expression> stack) throws AttributeParseException {}

    @Override
    public Operator getInstance() {
      return INSTANCE;
    }

    @Override
    public String getSymbol() {
      return String.valueOf(CLOSE_PARENTHESIS_CHAR);
    }

    @Override
    public int getPrecedence() {
      return PARENTHESIS_PRECEDENCE;
    }

    @Override
    public ExpressionType getExpressionType() {
      return ExpressionType.CLOSE_PARENTHESIS;
    }

  }
}
