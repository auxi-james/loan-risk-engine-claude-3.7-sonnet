package com.loanrisk.engine.evaluator;

import org.springframework.stereotype.Component;

/**
 * Evaluator for the ENDS_WITH operator
 */
@Component
public class EndsWithEvaluator extends AbstractRuleEvaluator {

    @Override
    public boolean evaluate(Object fieldValue, String ruleValue) {
        if (fieldValue == null || !(fieldValue instanceof String)) {
            return false;
        }
        
        String stringValue = (String) fieldValue;
        return stringValue.endsWith(ruleValue);
    }

    @Override
    public String getOperatorType() {
        return "ENDS_WITH";
    }
}