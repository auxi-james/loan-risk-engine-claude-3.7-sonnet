package com.loanrisk.service;

import com.loanrisk.engine.RuleEngine;
import com.loanrisk.engine.calculator.DerivedFieldCalculator;
import com.loanrisk.engine.determiner.RiskLevelDeterminer;
import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;
import com.loanrisk.model.entity.ScoringRule;
import com.loanrisk.service.impl.LoanEvaluationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class LoanEvaluationServiceTest {

    @Mock
    private RuleEngine ruleEngine;

    @Mock
    private DerivedFieldCalculator derivedFieldCalculator;

    @Mock
    private RiskLevelDeterminer riskLevelDeterminer;

    private LoanEvaluationService loanEvaluationService;
    private Customer customer;
    private LoanApplication loanApplication;
    private List<ScoringRule> triggeredRules;
    private Map<String, Object> derivedFields;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanEvaluationService = new LoanEvaluationServiceImpl(ruleEngine, derivedFieldCalculator, riskLevelDeterminer);
        
        // Create test customer
        customer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .age(35)
                .annualIncome(new BigDecimal("60000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("1000.00"))
                .build();
        
        // Create test loan application
        loanApplication = LoanApplication.builder()
                .id(1L)
                .customer(customer)
                .loanAmount(new BigDecimal("20000.00"))
                .loanPurpose("HOME_IMPROVEMENT")
                .requestedTermMonths(36)
                .build();
        
        // Create test rules
        ScoringRule rule1 = ScoringRule.builder()
                .id(1L)
                .name("High Credit Score")
                .field("creditScore")
                .operator("GREATER_THAN")
                .ruleValue("700")
                .riskPoints(10)
                .priority(1)
                .enabled(true)
                .build();
        
        ScoringRule rule2 = ScoringRule.builder()
                .id(2L)
                .name("Home Improvement Purpose")
                .field("loanPurpose")
                .operator("EQUALS")
                .ruleValue("HOME_IMPROVEMENT")
                .riskPoints(5)
                .priority(2)
                .enabled(true)
                .build();
        
        triggeredRules = Arrays.asList(rule1, rule2);
        
        // Create derived fields
        derivedFields = new HashMap<>();
        derivedFields.put("debtToIncomeRatio", new BigDecimal("0.2"));
        derivedFields.put("loanToIncomeRatio", new BigDecimal("0.33"));
        
        // Setup mocks
        when(derivedFieldCalculator.calculateDerivedFields(any(Customer.class), any(LoanApplication.class)))
                .thenReturn(derivedFields);
        
        when(ruleEngine.evaluateRules(any(LoanApplication.class), any(Customer.class), anyMap()))
                .thenReturn(triggeredRules);
        
        when(ruleEngine.calculateRiskScore(anyList()))
                .thenReturn(15);
        
        when(riskLevelDeterminer.determineRiskLevel(anyInt()))
                .thenReturn(RiskLevelDeterminer.RISK_LEVEL_LOW);
        
        when(riskLevelDeterminer.determineDecision(anyString()))
                .thenReturn(RiskLevelDeterminer.DECISION_APPROVE);
    }

    @Test
    void testEvaluateLoanApplication() {
        // Test the evaluation
        LoanApplication result = loanEvaluationService.evaluateLoanApplication(loanApplication);
        
        // Verify the result
        assertNotNull(result);
        assertEquals(15, result.getRiskScore());
        assertEquals(RiskLevelDeterminer.RISK_LEVEL_LOW, result.getRiskLevel());
        assertEquals(RiskLevelDeterminer.DECISION_APPROVE, result.getDecision());
        assertNotNull(result.getExplanation());
        
        // Verify interactions with dependencies
        verify(derivedFieldCalculator).calculateDerivedFields(customer, loanApplication);
        verify(ruleEngine).evaluateRules(loanApplication, customer, derivedFields);
        verify(ruleEngine, times(2)).calculateRiskScore(triggeredRules);
        verify(riskLevelDeterminer, times(2)).determineRiskLevel(15);
        verify(riskLevelDeterminer, times(2)).determineDecision(RiskLevelDeterminer.RISK_LEVEL_LOW);
    }

    @Test
    void testGetTriggeredRules() {
        List<ScoringRule> result = loanEvaluationService.getTriggeredRules(loanApplication);
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(triggeredRules, result);
        
        verify(derivedFieldCalculator).calculateDerivedFields(customer, loanApplication);
        verify(ruleEngine).evaluateRules(loanApplication, customer, derivedFields);
    }

    @Test
    void testGetDerivedFields() {
        Map<String, Object> result = loanEvaluationService.getDerivedFields(customer, loanApplication);
        
        assertNotNull(result);
        assertEquals(derivedFields, result);
        
        verify(derivedFieldCalculator).calculateDerivedFields(customer, loanApplication);
    }

    @Test
    void testGenerateExplanation() {
        String explanation = loanEvaluationService.generateExplanation(loanApplication, triggeredRules, derivedFields);
        
        assertNotNull(explanation);
        assertTrue(explanation.contains("Loan Application Evaluation"));
        assertTrue(explanation.contains("Customer Information"));
        assertTrue(explanation.contains("Derived Fields"));
        assertTrue(explanation.contains("Triggered Risk Factors"));
        assertTrue(explanation.contains("Risk Assessment"));
        
        // Verify specific content
        assertTrue(explanation.contains("High Credit Score"));
        assertTrue(explanation.contains("Home Improvement Purpose"));
        assertTrue(explanation.contains("Total Risk Score: 15"));
        
        verify(ruleEngine).calculateRiskScore(triggeredRules);
        verify(riskLevelDeterminer).determineRiskLevel(15);
        verify(riskLevelDeterminer).determineDecision(RiskLevelDeterminer.RISK_LEVEL_LOW);
    }
}