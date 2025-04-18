package com.loanrisk.engine.evaluator;

import org.springframework.stereotype.Component;

/**
 * Evaluator for the GREATER_THAN_OR_EQUAL operator
 */
@Component
public class GreaterThanOrEqualEvaluator extends AbstractRuleEvaluator {

    @Override
    public boolean evaluate(Object fieldValue, String ruleValue) {
        if (fieldValue == null) {
            return false;
        }
        
        Object convertedValue = convertFieldValue(fieldValue, ruleValue);
        if (convertedValue == null) {
            return false;
        }
        
        return compare(fieldValue, convertedValue) >= 0;
    }

    @Override
    public String getOperatorType() {
        return "GREATER_THAN_OR_EQUAL";
    }
}