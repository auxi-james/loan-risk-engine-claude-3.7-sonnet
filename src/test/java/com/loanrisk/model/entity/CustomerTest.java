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

class CustomerTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidCustomer() {
        // Given
        Customer customer = Customer.builder()
                .name("John Doe")
                .age(30)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .build();

        // When
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidName() {
        // Given
        Customer customer = Customer.builder()
                .name("")
                .age(30)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .build();

        // When
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Name is required", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidAge() {
        // Given
        Customer customer = Customer.builder()
                .name("John Doe")
                .age(15)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .build();

        // When
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Age must be at least 18", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidCreditScore() {
        // Given
        Customer customer = Customer.builder()
                .name("John Doe")
                .age(30)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(900)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .build();

        // When
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Credit score must be less than 850", violations.iterator().next().getMessage());
    }

    @Test
    void testNegativeAnnualIncome() {
        // Given
        Customer customer = Customer.builder()
                .name("John Doe")
                .age(30)
                .annualIncome(new BigDecimal("-1000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .build();

        // When
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Annual income must be positive", violations.iterator().next().getMessage());
    }

    @Test
    void testLifecycleMethods() {
        // Given
        Customer customer = new Customer();
        
        // When
        customer.onCreate();
        LocalDateTime createdAt = customer.getCreatedAt();
        LocalDateTime updatedAt = customer.getUpdatedAt();
        
        // Then
        assertNotNull(createdAt);
        assertNotNull(updatedAt);
        
        // When
        try {
            Thread.sleep(10); // Small delay to ensure time difference
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customer.onUpdate();
        
        // Then
        assertEquals(createdAt, customer.getCreatedAt());
        assertTrue(customer.getUpdatedAt().isAfter(updatedAt));
    }
}