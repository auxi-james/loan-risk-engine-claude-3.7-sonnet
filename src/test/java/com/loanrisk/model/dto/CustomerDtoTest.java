package com.loanrisk.model.dto;

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

class CustomerDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidCustomerRequestDto() {
        // Given
        CustomerRequestDto dto = CustomerRequestDto.builder()
                .name("John Doe")
                .age(30)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .build();

        // When
        Set<ConstraintViolation<CustomerRequestDto>> violations = validator.validate(dto);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidCustomerRequestDto() {
        // Given
        CustomerRequestDto dto = CustomerRequestDto.builder()
                .name("")
                .age(15)
                .annualIncome(new BigDecimal("-1000.00"))
                .creditScore(900)
                .employmentStatus("")
                .existingDebt(new BigDecimal("-500.00"))
                .build();

        // When
        Set<ConstraintViolation<CustomerRequestDto>> violations = validator.validate(dto);

        // Then
        assertEquals(6, violations.size());
    }

    @Test
    void testCustomerResponseDto() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        
        // When
        CustomerResponseDto dto = CustomerResponseDto.builder()
                .id(1L)
                .name("John Doe")
                .age(30)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertEquals(1L, dto.getId());
        assertEquals("John Doe", dto.getName());
        assertEquals(30, dto.getAge());
        assertEquals(new BigDecimal("75000.00"), dto.getAnnualIncome());
        assertEquals(720, dto.getCreditScore());
        assertEquals("EMPLOYED", dto.getEmploymentStatus());
        assertEquals(new BigDecimal("15000.00"), dto.getExistingDebt());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }
}