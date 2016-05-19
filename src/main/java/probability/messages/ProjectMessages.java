package probability.messages;

public interface ProjectMessages {

    /** Title of the mulligan rule description. */
    String mulliganRuleDescriptionTitle();

    String variablesDescriptionTitle();

    String grammarDescriptionString(String string, char quoteChar);

    String grammarDescriptionVariable(String variable);

    String grammarDescriptionExpression(String expression);

    String grammarDescriptionRule();
}
