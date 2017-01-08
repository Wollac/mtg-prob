package probability.rules.engine;

import probability.attr.ImmutableAttributeHolder;

/**
 * Class for a combination of parsed {@linkplain Token} object that can be evaluated with respected
 * to given variable bindings.
 */
public interface Expression {

  /**
   * Evaluates the expression into true or false.
   *
   * @param bindings bindings for all the variables contained in the expression
   */
  boolean interpret(ImmutableAttributeHolder bindings);

}
