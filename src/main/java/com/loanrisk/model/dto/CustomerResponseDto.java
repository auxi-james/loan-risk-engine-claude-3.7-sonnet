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
public class CustomerResponseDto {

    private Long id;
    private String name;
    private Integer age;
    private BigDecimal annualIncome;
    private Integer creditScore;
    private String employmentStatus;
    private BigDecimal existingDebt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}