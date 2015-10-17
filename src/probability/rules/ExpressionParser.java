package probability.rules;

import java.util.Stack;

import probability.attr.AttributeKey.AttributeParseException;
import probability.rules.Value.StringValue;

public class ExpressionParser {

  public static Expression fromString(String expr) throws AttributeParseException {

    Stack<Expression> stack = new Stack<>();

    String[] tokens = expr.split("\\s");
    for (String token : tokens) {
      Operations op = Operations.getOperation(token);
      if (op != null) {
        stack.push(op.createInstance());
      } else if (Variables.isRegistered(token)) {
        stack.push(Variables.getVariable(token));
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
