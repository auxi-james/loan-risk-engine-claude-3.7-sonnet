package com.loanrisk.engine.evaluator;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RuleEvaluatorTest {

    // Test Equals Evaluator
    @Test
    void testEqualsEvaluator() {
        EqualsEvaluator evaluator = new EqualsEvaluator();
        
        // Test with Integer
        assertTrue(evaluator.evaluate(100, "100"));
        assertFalse(evaluator.evaluate(100, "200"));
        
        // Test with BigDecimal
        assertTrue(evaluator.evaluate(new BigDecimal("100.50"), "100.50"));
        assertFalse(evaluator.evaluate(new BigDecimal("100.50"), "100.51"));
        
        // Test with String
        assertTrue(evaluator.evaluate("test", "test"));
        assertFalse(evaluator.evaluate("test", "other"));
        
        // Test with null
        assertFalse(evaluator.evaluate(null, "test"));
    }
    
    // Test NotEquals Evaluator
    @Test
    void testNotEqualsEvaluator() {
        NotEqualsEvaluator evaluator = new NotEqualsEvaluator();
        
        // Test with Integer
        assertTrue(evaluator.evaluate(100, "200"));
        assertFalse(evaluator.evaluate(100, "100"));
        
        // Test with BigDecimal
        assertTrue(evaluator.evaluate(new BigDecimal("100.50"), "100.51"));
        assertFalse(evaluator.evaluate(new BigDecimal("100.50"), "100.50"));
        
        // Test with String
        assertTrue(evaluator.evaluate("test", "other"));
        assertFalse(evaluator.evaluate("test", "test"));
        
        // Test with null
        assertFalse(evaluator.evaluate(null, "test"));
    }
    
    // Test GreaterThan Evaluator
    @Test
    void testGreaterThanEvaluator() {
        GreaterThanEvaluator evaluator = new GreaterThanEvaluator();
        
        // Test with Integer
        assertTrue(evaluator.evaluate(200, "100"));
        assertFalse(evaluator.evaluate(100, "100"));
        assertFalse(evaluator.evaluate(50, "100"));
        
        // Test with BigDecimal
        assertTrue(evaluator.evaluate(new BigDecimal("100.51"), "100.50"));
        assertFalse(evaluator.evaluate(new BigDecimal("100.50"), "100.50"));
        assertFalse(evaluator.evaluate(new BigDecimal("100.49"), "100.50"));
        
        // Test with null
        assertFalse(evaluator.evaluate(null, "100"));
    }
    
    // Test GreaterThanOrEqual Evaluator
    @Test
    void testGreaterThanOrEqualEvaluator() {
        GreaterThanOrEqualEvaluator evaluator = new GreaterThanOrEqualEvaluator();
        
        // Test with Integer
        assertTrue(evaluator.evaluate(200, "100"));
        assertTrue(evaluator.evaluate(100, "100"));
        assertFalse(evaluator.evaluate(50, "100"));
        
        // Test with BigDecimal
        assertTrue(evaluator.evaluate(new BigDecimal("100.51"), "100.50"));
        assertTrue(evaluator.evaluate(new BigDecimal("100.50"), "100.50"));
        assertFalse(evaluator.evaluate(new BigDecimal("100.49"), "100.50"));
        
        // Test with null
        assertFalse(evaluator.evaluate(null, "100"));
    }
    
    // Test LessThan Evaluator
    @Test
    void testLessThanEvaluator() {
        LessThanEvaluator evaluator = new LessThanEvaluator();
        
        // Test with Integer
        assertTrue(evaluator.evaluate(50, "100"));
        assertFalse(evaluator.evaluate(100, "100"));
        assertFalse(evaluator.evaluate(200, "100"));
        
        // Test with BigDecimal
        assertTrue(evaluator.evaluate(new BigDecimal("100.49"), "100.50"));
        assertFalse(evaluator.evaluate(new BigDecimal("100.50"), "100.50"));
        assertFalse(evaluator.evaluate(new BigDecimal("100.51"), "100.50"));
        
        // Test with null
        assertFalse(evaluator.evaluate(null, "100"));
    }
    
    // Test LessThanOrEqual Evaluator
    @Test
    void testLessThanOrEqualEvaluator() {
        LessThanOrEqualEvaluator evaluator = new LessThanOrEqualEvaluator();
        
        // Test with Integer
        assertTrue(evaluator.evaluate(50, "100"));
        assertTrue(evaluator.evaluate(100, "100"));
        assertFalse(evaluator.evaluate(200, "100"));
        
        // Test with BigDecimal
        assertTrue(evaluator.evaluate(new BigDecimal("100.49"), "100.50"));
        assertTrue(evaluator.evaluate(new BigDecimal("100.50"), "100.50"));
        assertFalse(evaluator.evaluate(new BigDecimal("100.51"), "100.50"));
        
        // Test with null
        assertFalse(evaluator.evaluate(null, "100"));
    }
    
    // Test Contains Evaluator
    @Test
    void testContainsEvaluator() {
        ContainsEvaluator evaluator = new ContainsEvaluator();
        
        // Test with String
        assertTrue(evaluator.evaluate("This is a test", "test"));
        assertFalse(evaluator.evaluate("This is a test", "other"));
        
        // Test with non-String
        assertFalse(evaluator.evaluate(100, "0"));
        
        // Test with null
        assertFalse(evaluator.evaluate(null, "test"));
    }
    
    // Test NotContains Evaluator
    @Test
    void testNotContainsEvaluator() {
        NotContainsEvaluator evaluator = new NotContainsEvaluator();
        
        // Test with String
        assertTrue(evaluator.evaluate("This is a test", "other"));
        assertFalse(evaluator.evaluate("This is a test", "test"));
        
        // Test with non-String
        assertFalse(evaluator.evaluate(100, "0"));
        
        // Test with null
        assertFalse(evaluator.evaluate(null, "test"));
    }
    
    // Test StartsWith Evaluator
    @Test
    void testStartsWithEvaluator() {
        StartsWithEvaluator evaluator = new StartsWithEvaluator();
        
        // Test with String
        assertTrue(evaluator.evaluate("This is a test", "This"));
        assertFalse(evaluator.evaluate("This is a test", "That"));
        
        // Test with non-String
        assertFalse(evaluator.evaluate(100, "1"));
        
        // Test with null
        assertFalse(evaluator.evaluate(null, "This"));
    }
    
    // Test EndsWith Evaluator
    @Test
    void testEndsWithEvaluator() {
        EndsWithEvaluator evaluator = new EndsWithEvaluator();
        
        // Test with String
        assertTrue(evaluator.evaluate("This is a test", "test"));
        assertFalse(evaluator.evaluate("This is a test", "This"));
        
        // Test with non-String
        assertFalse(evaluator.evaluate(100, "0"));
        
        // Test with null
        assertFalse(evaluator.evaluate(null, "test"));
    }
}