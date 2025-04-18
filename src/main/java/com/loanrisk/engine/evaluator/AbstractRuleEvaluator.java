package com.loanrisk.engine.evaluator;

import com.loanrisk.engine.RuleEvaluator;

import java.math.BigDecimal;

/**
 * Abstract base class for rule evaluators
 */
public abstract class AbstractRuleEvaluator implements RuleEvaluator {

    /**
     * Convert field value to appropriate type for comparison
     */
    protected Object convertFieldValue(Object fieldValue, String ruleValue) {
        if (fieldValue instanceof Integer) {
            try {
                return Integer.parseInt(ruleValue);
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (fieldValue instanceof BigDecimal) {
            try {
                return new BigDecimal(ruleValue);
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (fieldValue instanceof String) {
            return ruleValue;
        } else if (fieldValue instanceof Boolean) {
            return Boolean.parseBoolean(ruleValue);
        }
        
        return null;
    }
    
    /**
     * Compare two objects for equality
     */
    @SuppressWarnings("unchecked")
    protected int compare(Object fieldValue, Object ruleValue) {
        if (fieldValue instanceof Integer && ruleValue instanceof Integer) {
            return ((Integer) fieldValue).compareTo((Integer) ruleValue);
        } else if (fieldValue instanceof BigDecimal && ruleValue instanceof BigDecimal) {
            return ((BigDecimal) fieldValue).compareTo((BigDecimal) ruleValue);
        } else if (fieldValue instanceof String && ruleValue instanceof String) {
            return ((String) fieldValue).compareTo((String) ruleValue);
        } else if (fieldValue instanceof Comparable && ruleValue instanceof Comparable 
                && fieldValue.getClass().equals(ruleValue.getClass())) {
            return ((Comparable<Object>) fieldValue).compareTo(ruleValue);
        }
        
        throw new IllegalArgumentException("Cannot compare values of different types");
    }
}