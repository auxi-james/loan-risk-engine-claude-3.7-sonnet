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

class LoanApplicationDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidLoanApplicationRequestDto() {
        // Given
        LoanApplicationRequestDto dto = LoanApplicationRequestDto.builder()
                .customerId(1L)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("HOME_IMPROVEMENT")
                .requestedTermMonths(60)
                .build();

        // When
        Set<ConstraintViolation<LoanApplicationRequestDto>> violations = validator.validate(dto);

        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidLoanApplicationRequestDto() {
        // Given
        LoanApplicationRequestDto dto = LoanApplicationRequestDto.builder()
                .customerId(null)
                .loanAmount(new BigDecimal("500.00"))
                .loanPurpose("")
                .requestedTermMonths(3)
                .build();

        // When
        Set<ConstraintViolation<LoanApplicationRequestDto>> violations = validator.validate(dto);

        // Then
        assertEquals(4, violations.size());
    }

    @Test
    void testLoanApplicationResponseDto() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        CustomerResponseDto customer = CustomerResponseDto.builder()
                .id(1L)
                .name("John Doe")
                .build();
        
        // When
        LoanApplicationResponseDto dto = LoanApplicationResponseDto.builder()
                .id(1L)
                .customerId(1L)
                .customer(customer)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("HOME_IMPROVEMENT")
                .requestedTermMonths(60)
                .riskScore(35)
                .riskLevel("MEDIUM")
                .decision("APPROVED")
                .explanation("Loan approved with conditions")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getCustomerId());
        assertEquals(customer, dto.getCustomer());
        assertEquals(new BigDecimal("25000.00"), dto.getLoanAmount());
        assertEquals("HOME_IMPROVEMENT", dto.getLoanPurpose());
        assertEquals(60, dto.getRequestedTermMonths());
        assertEquals(35, dto.getRiskScore());
        assertEquals("MEDIUM", dto.getRiskLevel());
        assertEquals("APPROVED", dto.getDecision());
        assertEquals("Loan approved with conditions", dto.getExplanation());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }
}