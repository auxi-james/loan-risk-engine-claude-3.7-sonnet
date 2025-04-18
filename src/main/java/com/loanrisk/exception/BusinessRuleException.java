package com.loanrisk.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a business rule is violated
 */
public class BusinessRuleException extends CustomException {
    
    private static final HttpStatus STATUS = HttpStatus.UNPROCESSABLE_ENTITY;
    private static final String ERROR_CODE = "BUSINESS_RULE_VIOLATION";
    
    public BusinessRuleException(String message) {
        super(message, STATUS, ERROR_CODE);
    }
    
    public BusinessRuleException(String message, Throwable cause) {
        super(message, STATUS, ERROR_CODE, cause);
    }
    
    public BusinessRuleException(String ruleName, String ruleDescription) {
        super(String.format("Business rule '%s' violated: %s", ruleName, ruleDescription), 
              STATUS, ERROR_CODE);
    }
}