package com.loanrisk.engine.evaluator;

import org.springframework.stereotype.Component;

/**
 * Evaluator for the STARTS_WITH operator
 */
@Component
public class StartsWithEvaluator extends AbstractRuleEvaluator {

    @Override
    public boolean evaluate(Object fieldValue, String ruleValue) {
        if (fieldValue == null || !(fieldValue instanceof String)) {
            return false;
        }
        
        String stringValue = (String) fieldValue;
        return stringValue.startsWith(ruleValue);
    }

    @Override
    public String getOperatorType() {
        return "STARTS_WITH";
    }
}