package com.loanrisk.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when the request is invalid
 */
public class BadRequestException extends CustomException {
    
    private static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;
    private static final String ERROR_CODE = "BAD_REQUEST";
    
    public BadRequestException(String message) {
        super(message, STATUS, ERROR_CODE);
    }
    
    public BadRequestException(String message, Throwable cause) {
        super(message, STATUS, ERROR_CODE, cause);
    }
}