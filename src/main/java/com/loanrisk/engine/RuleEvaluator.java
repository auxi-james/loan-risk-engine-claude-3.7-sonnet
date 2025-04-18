package com.loanrisk.engine;

/**
 * Interface for evaluating rules with different operators
 */
public interface RuleEvaluator {
    
    /**
     * Evaluate a field value against a rule value using a specific operator
     * 
     * @param fieldValue the value of the field to evaluate
     * @param ruleValue the value to compare against
     * @return true if the rule is triggered, false otherwise
     */
    boolean evaluate(Object fieldValue, String ruleValue);
    
    /**
     * Get the operator type this evaluator handles
     * 
     * @return the operator type
     */
    String getOperatorType();
}