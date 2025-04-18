package com.loanrisk.engine.evaluator;

import org.springframework.stereotype.Component;

/**
 * Evaluator for the NOT_EQUALS operator
 */
@Component
public class NotEqualsEvaluator extends AbstractRuleEvaluator {

    @Override
    public boolean evaluate(Object fieldValue, String ruleValue) {
        if (fieldValue == null) {
            return false;
        }
        
        Object convertedValue = convertFieldValue(fieldValue, ruleValue);
        if (convertedValue == null) {
            return false;
        }
        
        return compare(fieldValue, convertedValue) != 0;
    }

    @Override
    public String getOperatorType() {
        return "NOT_EQUALS";
    }
}