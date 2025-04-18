package com.loanrisk.engine.factory;

import com.loanrisk.engine.RuleEvaluator;

/**
 * Factory interface for creating rule evaluators based on operator type
 */
public interface RuleEvaluatorFactory {
    
    /**
     * Get the appropriate rule evaluator for the given operator
     * 
     * @param operator the operator type
     * @return the rule evaluator for the operator, or null if no evaluator is found
     */
    RuleEvaluator getEvaluator(String operator);
}