package com.loanrisk.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 120, message = "Age must be less than 120")
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotNull(message = "Annual income is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Annual income must be positive")
    @Column(name = "annual_income", nullable = false, precision = 19, scale = 2)
    private BigDecimal annualIncome;

    @NotNull(message = "Credit score is required")
    @Min(value = 300, message = "Credit score must be at least 300")
    @Max(value = 850, message = "Credit score must be less than 850")
    @Column(name = "credit_score", nullable = false)
    private Integer creditScore;

    @NotBlank(message = "Employment status is required")
    @Size(max = 50, message = "Employment status must be less than 50 characters")
    @Column(name = "employment_status", nullable = false)
    private String employmentStatus;

    @NotNull(message = "Existing debt is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Existing debt must be positive or zero")
    @Column(name = "existing_debt", nullable = false, precision = 19, scale = 2)
    private BigDecimal existingDebt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}