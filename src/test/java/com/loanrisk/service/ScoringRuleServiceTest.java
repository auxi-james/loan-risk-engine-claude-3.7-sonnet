package com.loanrisk.service;

import com.loanrisk.exception.ResourceNotFoundException;
import com.loanrisk.model.dto.ScoringRuleResponseDto;
import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;
import com.loanrisk.model.entity.ScoringRule;
import com.loanrisk.repository.ScoringRuleRepository;
import com.loanrisk.service.impl.ScoringRuleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScoringRuleServiceTest {

    @Mock
    private ScoringRuleRepository scoringRuleRepository;

    @InjectMocks
    private ScoringRuleServiceImpl scoringRuleService;

    private ScoringRule scoringRule1;
    private ScoringRule scoringRule2;
    private Customer customer;
    private LoanApplication loanApplication;
    private final Long ruleId1 = 1L;
    private final Long ruleId2 = 2L;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Setup test scoring rules
        scoringRule1 = ScoringRule.builder()
                .id(ruleId1)
                .name("Low Credit Score Rule")
                .field("creditScore")
                .operator("LESS_THAN")
                .ruleValue("650")
                .riskPoints(30)
                .priority(1)
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        scoringRule2 = ScoringRule.builder()
                .id(ruleId2)
                .name("High Debt-to-Income Rule")
                .field("debtToIncomeRatio")
                .operator("GREATER_THAN")
                .ruleValue("0.4")
                .riskPoints(25)
                .priority(2)
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Setup test customer
        customer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .age(35)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(620)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("35000.00"))
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Setup test loan application
        loanApplication = LoanApplication.builder()
                .id(1L)
                .customer(customer)
                .loanAmount(new BigDecimal("150000.00"))
                .loanPurpose("HOME_PURCHASE")
                .requestedTermMonths(360)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Test
    void getScoringRuleById_WithValidId_ShouldReturnScoringRuleResponseDto() {
        // Arrange
        when(scoringRuleRepository.findById(ruleId1)).thenReturn(Optional.of(scoringRule1));

        // Act
        ScoringRuleResponseDto result = scoringRuleService.getScoringRuleById(ruleId1);

        // Assert
        assertNotNull(result);
        assertEquals(ruleId1, result.getId());
        assertEquals(scoringRule1.getName(), result.getName());
        assertEquals(scoringRule1.getField(), result.getField());
        assertEquals(scoringRule1.getOperator(), result.getOperator());
        assertEquals(scoringRule1.getRuleValue(), result.getRuleValue());
        assertEquals(scoringRule1.getRiskPoints(), result.getRiskPoints());
        assertEquals(scoringRule1.getPriority(), result.getPriority());
        assertEquals(scoringRule1.getEnabled(), result.getEnabled());
        verify(scoringRuleRepository, times(1)).findById(ruleId1);
    }

    @Test
    void getScoringRuleById_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(scoringRuleRepository.findById(ruleId1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            scoringRuleService.getScoringRuleById(ruleId1));
        verify(scoringRuleRepository, times(1)).findById(ruleId1);
    }

    @Test
    void getAllScoringRules_ShouldReturnListOfScoringRuleResponseDto() {
        // Arrange
        when(scoringRuleRepository.findAll()).thenReturn(Arrays.asList(scoringRule1, scoringRule2));

        // Act
        List<ScoringRuleResponseDto> result = scoringRuleService.getAllScoringRules();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(ruleId1, result.get(0).getId());
        assertEquals(ruleId2, result.get(1).getId());
        verify(scoringRuleRepository, times(1)).findAll();
    }

    @Test
    void findEnabledRules_ShouldReturnListOfEnabledRules() {
        // Arrange
        when(scoringRuleRepository.findByEnabledTrue()).thenReturn(Arrays.asList(scoringRule1, scoringRule2));

        // Act
        List<ScoringRuleResponseDto> result = scoringRuleService.findEnabledRules();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).getEnabled());
        assertTrue(result.get(1).getEnabled());
        verify(scoringRuleRepository, times(1)).findByEnabledTrue();
    }

    @Test
    void findDisabledRules_ShouldReturnListOfDisabledRules() {
        // Arrange
        ScoringRule disabledRule = ScoringRule.builder()
                .id(3L)
                .name("Disabled Rule")
                .field("age")
                .operator("LESS_THAN")
                .ruleValue("25")
                .riskPoints(15)
                .priority(3)
                .enabled(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        when(scoringRuleRepository.findByEnabledFalse()).thenReturn(Arrays.asList(disabledRule));

        // Act
        List<ScoringRuleResponseDto> result = scoringRuleService.findDisabledRules();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertFalse(result.get(0).getEnabled());
        verify(scoringRuleRepository, times(1)).findByEnabledFalse();
    }

    @Test
    void findRulesByField_ShouldReturnMatchingRules() {
        // Arrange
        String field = "creditScore";
        when(scoringRuleRepository.findByField(field)).thenReturn(Arrays.asList(scoringRule1));

        // Act
        List<ScoringRuleResponseDto> result = scoringRuleService.findRulesByField(field);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(field, result.get(0).getField());
        verify(scoringRuleRepository, times(1)).findByField(field);
    }

    @Test
    void enableRule_WithValidId_ShouldEnableRule() {
        // Arrange
        ScoringRule disabledRule = ScoringRule.builder()
                .id(ruleId1)
                .name("Low Credit Score Rule")
                .field("creditScore")
                .operator("LESS_THAN")
                .ruleValue("650")
                .riskPoints(30)
                .priority(1)
                .enabled(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        ScoringRule enabledRule = ScoringRule.builder()
                .id(ruleId1)
                .name("Low Credit Score Rule")
                .field("creditScore")
                .operator("LESS_THAN")
                .ruleValue("650")
                .riskPoints(30)
                .priority(1)
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        when(scoringRuleRepository.findById(ruleId1)).thenReturn(Optional.of(disabledRule));
        when(scoringRuleRepository.save(any(ScoringRule.class))).thenReturn(enabledRule);

        // Act
        ScoringRuleResponseDto result = scoringRuleService.enableRule(ruleId1);

        // Assert
        assertNotNull(result);
        assertEquals(ruleId1, result.getId());
        assertTrue(result.getEnabled());
        verify(scoringRuleRepository, times(1)).findById(ruleId1);
        verify(scoringRuleRepository, times(1)).save(any(ScoringRule.class));
    }

    @Test
    void disableRule_WithValidId_ShouldDisableRule() {
        // Arrange
        ScoringRule disabledRule = ScoringRule.builder()
                .id(ruleId1)
                .name("Low Credit Score Rule")
                .field("creditScore")
                .operator("LESS_THAN")
                .ruleValue("650")
                .riskPoints(30)
                .priority(1)
                .enabled(false)
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        when(scoringRuleRepository.findById(ruleId1)).thenReturn(Optional.of(scoringRule1));
        when(scoringRuleRepository.save(any(ScoringRule.class))).thenReturn(disabledRule);

        // Act
        ScoringRuleResponseDto result = scoringRuleService.disableRule(ruleId1);

        // Assert
        assertNotNull(result);
        assertEquals(ruleId1, result.getId());
        assertFalse(result.getEnabled());
        verify(scoringRuleRepository, times(1)).findById(ruleId1);
        verify(scoringRuleRepository, times(1)).save(any(ScoringRule.class));
    }

    @Test
    void applyRules_ShouldReturnTotalRiskScore() {
        // Arrange
        when(scoringRuleRepository.findByEnabledTrueOrderByPriorityAsc())
            .thenReturn(Arrays.asList(scoringRule1, scoringRule2));

        // Act
        Integer result = scoringRuleService.applyRules(loanApplication);

        // Assert
        assertNotNull(result);
        // Both rules should apply: creditScore < 650 and debtToIncomeRatio > 0.4
        // So total risk score should be 30 + 25 = 55
        assertEquals(55, result);
        verify(scoringRuleRepository, times(1)).findByEnabledTrueOrderByPriorityAsc();
    }

    @Test
    void getApplicableRules_ShouldReturnMatchingRules() {
        // Arrange
        when(scoringRuleRepository.findByEnabledTrueOrderByPriorityAsc())
            .thenReturn(Arrays.asList(scoringRule1, scoringRule2));

        // Act
        List<ScoringRule> result = scoringRuleService.getApplicableRules(loanApplication);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(ruleId1, result.get(0).getId());
        assertEquals(ruleId2, result.get(1).getId());
        verify(scoringRuleRepository, times(1)).findByEnabledTrueOrderByPriorityAsc();
    }
}