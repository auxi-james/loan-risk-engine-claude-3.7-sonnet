package com.loanrisk.engine.evaluator;

import org.springframework.stereotype.Component;

/**
 * Evaluator for the NOT_CONTAINS operator
 */
@Component
public class NotContainsEvaluator extends AbstractRuleEvaluator {

    @Override
    public boolean evaluate(Object fieldValue, String ruleValue) {
        if (fieldValue == null || !(fieldValue instanceof String)) {
            return false;
        }
        
        String stringValue = (String) fieldValue;
        return !stringValue.contains(ruleValue);
    }

    @Override
    public String getOperatorType() {
        return "NOT_CONTAINS";
    }
}