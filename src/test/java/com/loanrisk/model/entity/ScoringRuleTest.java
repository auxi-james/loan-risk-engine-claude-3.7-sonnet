package com.loanrisk.model.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ScoringRuleTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidScoringRule() {
        // Given
        ScoringRule scoringRule = ScoringRule.builder()
                .name("Credit too low")
                .field("creditScore")
                .operator("<")
                .ruleValue("600")
                .riskPoints(30)
                .priority(1)
                .enabled(true)
                .build();

        // When
        Set<ConstraintViolation<ScoringRule>> violations = validator.validate(scoringRule);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testBlankName() {
        // Given
        ScoringRule scoringRule = ScoringRule.builder()
                .name("")
                .field("creditScore")
                .operator("<")
                .ruleValue("600")
                .riskPoints(30)
                .priority(1)
                .enabled(true)
                .build();

        // When
        Set<ConstraintViolation<ScoringRule>> violations = validator.validate(scoringRule);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankField() {
        // Given
        ScoringRule scoringRule = ScoringRule.builder()
                .name("Credit too low")
                .field("")
                .operator("<")
                .ruleValue("600")
                .riskPoints(30)
                .priority(1)
                .enabled(true)
                .build();

        // When
        Set<ConstraintViolation<ScoringRule>> violations = validator.validate(scoringRule);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Field is required", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankOperator() {
        // Given
        ScoringRule scoringRule = ScoringRule.builder()
                .name("Credit too low")
                .field("creditScore")
                .operator("")
                .ruleValue("600")
                .riskPoints(30)
                .priority(1)
                .enabled(true)
                .build();

        // When
        Set<ConstraintViolation<ScoringRule>> violations = validator.validate(scoringRule);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Operator is required", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankRuleValue() {
        // Given
        ScoringRule scoringRule = ScoringRule.builder()
                .name("Credit too low")
                .field("creditScore")
                .operator("<")
                .ruleValue("")
                .riskPoints(30)
                .priority(1)
                .enabled(true)
                .build();

        // When
        Set<ConstraintViolation<ScoringRule>> violations = validator.validate(scoringRule);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Rule value is required", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidRiskPoints() {
        // Given
        ScoringRule scoringRule = ScoringRule.builder()
                .name("Credit too low")
                .field("creditScore")
                .operator("<")
                .ruleValue("600")
                .riskPoints(101) // Over the max of 100
                .priority(1)
                .enabled(true)
                .build();

        // When
        Set<ConstraintViolation<ScoringRule>> violations = validator.validate(scoringRule);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Risk points must be less than 100", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidPriority() {
        // Given
        ScoringRule scoringRule = ScoringRule.builder()
                .name("Credit too low")
                .field("creditScore")
                .operator("<")
                .ruleValue("600")
                .riskPoints(30)
                .priority(0) // Below the min of 1
                .enabled(true)
                .build();

        // When
        Set<ConstraintViolation<ScoringRule>> violations = validator.validate(scoringRule);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Priority must be at least 1", violations.iterator().next().getMessage());
    }

    @Test
    void testDefaultEnabled() {
        // Given
        ScoringRule scoringRule = ScoringRule.builder()
                .name("Credit too low")
                .field("creditScore")
                .operator("<")
                .ruleValue("600")
                .riskPoints(30)
                .priority(1)
                .build(); // No enabled value provided

        // Then
        assertTrue(scoringRule.getEnabled()); // Should default to true
    }

    @Test
    void testLifecycleMethods() {
        // Given
        ScoringRule scoringRule = new ScoringRule();
        
        // When
        scoringRule.onCreate();
        LocalDateTime createdAt = scoringRule.getCreatedAt();
        LocalDateTime updatedAt = scoringRule.getUpdatedAt();
        
        // Then
        assertNotNull(createdAt);
        assertNotNull(updatedAt);
        
        // When
        try {
            Thread.sleep(10); // Small delay to ensure time difference
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scoringRule.onUpdate();
        
        // Then
        assertEquals(createdAt, scoringRule.getCreatedAt());
        assertTrue(scoringRule.getUpdatedAt().isAfter(updatedAt));
    }
}