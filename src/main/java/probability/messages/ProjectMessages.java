package probability.messages;

public interface ProjectMessages {

  /**
   * Title of the mulligan rule description.
   */
  String mulliganRuleDescriptionTitle();

  String variablesDescriptionTitle();

  /**
   * Definition of a string in the rules grammar.
   */
  String grammarDescriptionString(String string, char quoteChar);

  /**
   * Definition of a variable in the rules grammar.
   */
  String grammarDescriptionVariable(String variable);

  /**
   * Definition of an expression in the rules grammar.
   */
  String grammarDescriptionExpression(String expression);

  String grammarDescriptionRule();

  String probability(int turn, double probability);

  String combinedFailureProbability();

  String takeMulligan();

  String readFileException(String fileName, String fileType, String exceptionMessages);

  String parseFileException(String fileName, String fileType, String exceptionMessages);

  String parseFileExceptionWithLineNumber(String fileName, String fileType,
      String exceptionMessages, int errorLine);

  String writeDefaultFile(String fileName, String fileType);
}
