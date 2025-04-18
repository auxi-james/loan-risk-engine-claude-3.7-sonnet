package com.loanrisk.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationRequestDto {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Loan amount is required")
    @DecimalMin(value = "1000.0", inclusive = true, message = "Loan amount must be at least 1000")
    private BigDecimal loanAmount;

    @NotBlank(message = "Loan purpose is required")
    @Size(max = 100, message = "Loan purpose must be less than 100 characters")
    private String loanPurpose;

    @NotNull(message = "Requested term months is required")
    @Min(value = 6, message = "Requested term months must be at least 6")
    @Max(value = 360, message = "Requested term months must be less than 360")
    private Integer requestedTermMonths;
}