package probability.rules;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import probability.attr.AttributeKey;
import probability.attr.AttributeKey.AttributeParseException;
import probability.rules.Value.StringValue;

public class ExpressionParser {

  private final Map<String, AttributeKey<?>> _name2Key;

  public ExpressionParser(Set<? extends AttributeKey<?>> keySet) {

    _name2Key = new HashMap<>();

    for (AttributeKey<?> key : keySet) {
      _name2Key.put(key.getName(), key);
    }

  }

  public Expression fromString(String expr) throws AttributeParseException {
    Stack<Expression> stack = new Stack<>();

    String[] tokens = expr.split("\\s");
    for (String token : tokens) {
      Operations op = Operations.getOperation(token);
      if (op != null) {

        stack.push(op.createInstance());
      } else if (_name2Key.containsKey(token)) {

        stack.push(Variable.createVariable(_name2Key.get(token)));
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
