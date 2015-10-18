package probability.rules;

import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;
import probability.rules.Value.StringValue;

public class ExpressionParser {

  public static Expression parseRPNString(VariableHolder variables, String expression)
      throws AttributeParseException {

    Stack<Expression> stack = new Stack<>();

    String[] tokens = expression.split("\\s");
    for (String token : tokens) {
      Operations op = Operations.getOperation(token);
      if (op != null) {
        stack.push(op.createInstance());
      } else if (variables.isRegistered(token)) {
        stack.push(variables.getVariable(token));
      } else {
        stack.push(new StringValue(token));
      }
    }

    Expression top = stack.pop();
    top.parse(stack);

    if (!stack.isEmpty()) {
      throw new IllegalStateException();
    }

    return top;
  }

}
