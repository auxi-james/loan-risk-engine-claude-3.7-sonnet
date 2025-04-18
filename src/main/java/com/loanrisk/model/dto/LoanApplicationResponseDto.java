package com.loanrisk.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationResponseDto {

    private Long id;
    private Long customerId;
    private CustomerResponseDto customer;
    private BigDecimal loanAmount;
    private String loanPurpose;
    private Integer requestedTermMonths;
    private Integer riskScore;
    private String riskLevel;
    private String decision;
    private String explanation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}