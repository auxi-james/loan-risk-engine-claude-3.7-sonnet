package com.loanrisk.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "scoring_rule")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScoringRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Field is required")
    @Size(max = 100, message = "Field must be less than 100 characters")
    @Column(name = "field", nullable = false)
    private String field;

    @NotBlank(message = "Operator is required")
    @Size(max = 50, message = "Operator must be less than 50 characters")
    @Column(name = "operator", nullable = false)
    private String operator;

    @NotBlank(message = "Rule value is required")
    @Size(max = 255, message = "Rule value must be less than 255 characters")
    @Column(name = "rule_value", nullable = false)
    private String ruleValue;

    @NotNull(message = "Risk points is required")
    @Min(value = 0, message = "Risk points must be at least 0")
    @Max(value = 100, message = "Risk points must be less than 100")
    @Column(name = "risk_points", nullable = false)
    private Integer riskPoints;

    @NotNull(message = "Priority is required")
    @Min(value = 1, message = "Priority must be at least 1")
    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Builder.Default
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

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