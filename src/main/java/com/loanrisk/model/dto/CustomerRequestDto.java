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
public class CustomerRequestDto {

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 120, message = "Age must be less than 120")
    private Integer age;

    @NotNull(message = "Annual income is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Annual income must be positive")
    private BigDecimal annualIncome;

    @NotNull(message = "Credit score is required")
    @Min(value = 300, message = "Credit score must be at least 300")
    @Max(value = 850, message = "Credit score must be less than 850")
    private Integer creditScore;

    @NotBlank(message = "Employment status is required")
    @Size(max = 50, message = "Employment status must be less than 50 characters")
    private String employmentStatus;

    @NotNull(message = "Existing debt is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Existing debt must be positive or zero")
    private BigDecimal existingDebt;
}