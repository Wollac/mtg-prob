package probability.rules;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import probability.attr.AttributeKey;
import probability.attr.AttributeKey.AttributeParseException;

public class ExpressionParser
{
    private static final Operations operations = Operations.INSTANCE;
    
    private final Map<String, AttributeKey<?>> _name2Key;
    
    public ExpressionParser(Set<? extends AttributeKey<?>> keySet) {
    	
    	_name2Key = new HashMap<>();
    	
    	for (AttributeKey<?> key : keySet) {
    		_name2Key.put(key.getName(), key);
    	}
    	
	}

    public Expression fromString(String expr) throws AttributeParseException
    {
        Stack<Expression> stack = new Stack<>();

        String[] tokens = expr.split("\\s");
        for (int i=0; i < tokens.length-1; i++)
        {
            Operation op = operations.getOperation(tokens[i]);
            if ( op != null )
            {
                // create a new instance
                op = op.copy();
                i = op.parse(tokens, i, _name2Key, stack);
            }
        }

        return stack.pop();
    }
}