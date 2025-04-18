package com.loanrisk.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test controller for exception handling tests
 */
@RestController
@RequestMapping("/test")
public class TestController {
    
    @GetMapping("/resource-not-found/{id}")
    public String resourceNotFound(@PathVariable Long id) {
        throw new ResourceNotFoundException("Resource", "id", id);
    }
    
    @GetMapping("/bad-request")
    public String badRequest() {
        throw new BadRequestException("This is a bad request");
    }
    
    @GetMapping("/business-rule")
    public String businessRule() {
        throw new BusinessRuleException("Test Rule", "This is a business rule violation");
    }
    
    @GetMapping("/generic-error")
    public String genericError() {
        throw new RuntimeException("This is a generic error");
    }
}