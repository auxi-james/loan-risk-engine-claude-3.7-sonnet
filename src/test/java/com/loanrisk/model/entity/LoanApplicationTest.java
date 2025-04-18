package com.loanrisk.model.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoanApplicationTest {

    private Validator validator;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        testCustomer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .age(30)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .build();
    }

    @Test
    void testValidLoanApplication() {
        // Given
        LoanApplication loanApplication = LoanApplication.builder()
                .customer(testCustomer)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("HOME_IMPROVEMENT")
                .requestedTermMonths(60)
                .build();

        // When
        Set<ConstraintViolation<LoanApplication>> violations = validator.validate(loanApplication);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNullCustomer() {
        // Given
        LoanApplication loanApplication = LoanApplication.builder()
                .customer(null)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("HOME_IMPROVEMENT")
                .requestedTermMonths(60)
                .build();

        // When
        Set<ConstraintViolation<LoanApplication>> violations = validator.validate(loanApplication);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Customer is required", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidLoanAmount() {
        // Given
        LoanApplication loanApplication = LoanApplication.builder()
                .customer(testCustomer)
                .loanAmount(new BigDecimal("500.00"))
                .loanPurpose("HOME_IMPROVEMENT")
                .requestedTermMonths(60)
                .build();

        // When
        Set<ConstraintViolation<LoanApplication>> violations = validator.validate(loanApplication);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Loan amount must be at least 1000", violations.iterator().next().getMessage());
    }

    @Test
    void testBlankLoanPurpose() {
        // Given
        LoanApplication loanApplication = LoanApplication.builder()
                .customer(testCustomer)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("")
                .requestedTermMonths(60)
                .build();

        // When
        Set<ConstraintViolation<LoanApplication>> violations = validator.validate(loanApplication);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Loan purpose is required", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidTermMonths() {
        // Given
        LoanApplication loanApplication = LoanApplication.builder()
                .customer(testCustomer)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("HOME_IMPROVEMENT")
                .requestedTermMonths(3)
                .build();

        // When
        Set<ConstraintViolation<LoanApplication>> violations = validator.validate(loanApplication);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Requested term months must be at least 6", violations.iterator().next().getMessage());
    }

    @Test
    void testLifecycleMethods() {
        // Given
        LoanApplication loanApplication = new LoanApplication();
        
        // When
        loanApplication.onCreate();
        LocalDateTime createdAt = loanApplication.getCreatedAt();
        LocalDateTime updatedAt = loanApplication.getUpdatedAt();
        
        // Then
        assertNotNull(createdAt);
        assertNotNull(updatedAt);
        
        // When
        try {
            Thread.sleep(10); // Small delay to ensure time difference
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loanApplication.onUpdate();
        
        // Then
        assertEquals(createdAt, loanApplication.getCreatedAt());
        assertTrue(loanApplication.getUpdatedAt().isAfter(updatedAt));
    }
}