package com.loanrisk.engine;

import com.loanrisk.engine.factory.RuleEvaluatorFactory;
import com.loanrisk.engine.impl.RuleEngineImpl;
import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;
import com.loanrisk.model.entity.ScoringRule;
import com.loanrisk.repository.ScoringRuleRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class RuleEngineTest {

    @Mock
    private ScoringRuleRepository scoringRuleRepository;

    @Mock
    private RuleEvaluatorFactory ruleEvaluatorFactory;

    @Mock
    private RuleEvaluator equalsEvaluator;

    @Mock
    private RuleEvaluator greaterThanEvaluator;

    private RuleEngine ruleEngine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ruleEngine = new RuleEngineImpl(scoringRuleRepository, ruleEvaluatorFactory);
        
        // Setup mock evaluators
        when(ruleEvaluatorFactory.getEvaluator("EQUALS")).thenReturn(equalsEvaluator);
        when(ruleEvaluatorFactory.getEvaluator("GREATER_THAN")).thenReturn(greaterThanEvaluator);
    }

    @Test
    void testEvaluateRules() {
        // Create test data
        Customer customer = Customer.builder()
                .name("John Doe")
                .age(35)
                .annualIncome(new BigDecimal("60000"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("10000"))
                .build();
        
        LoanApplication loanApplication = LoanApplication.builder()
                .customer(customer)
                .loanAmount(new BigDecimal("20000"))
                .loanPurpose("HOME_IMPROVEMENT")
                .requestedTermMonths(36)
                .build();
        
        Map<String, Object> derivedFields = new HashMap<>();
        derivedFields.put("debtToIncomeRatio", new BigDecimal("0.2"));
        derivedFields.put("loanToIncomeRatio", new BigDecimal("0.33"));
        
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
        
        // Setup mock repository
        when(scoringRuleRepository.findByEnabledTrueOrderByPriorityAsc())
                .thenReturn(Arrays.asList(rule1, rule2));
        
        // Setup mock evaluators
        when(greaterThanEvaluator.evaluate(customer.getCreditScore(), "700")).thenReturn(true);
        when(equalsEvaluator.evaluate(loanApplication.getLoanPurpose(), "HOME_IMPROVEMENT")).thenReturn(true);
        
        // Test rule evaluation
        List<ScoringRule> triggeredRules = ruleEngine.evaluateRules(loanApplication, customer, derivedFields);
        
        // Verify results
        assertNotNull(triggeredRules);
        assertEquals(2, triggeredRules.size());
        assertTrue(triggeredRules.contains(rule1));
        assertTrue(triggeredRules.contains(rule2));
    }

    @Test
    void testCalculateRiskScore() {
        // Create test rules
        ScoringRule rule1 = ScoringRule.builder()
                .id(1L)
                .name("High Credit Score")
                .riskPoints(10)
                .build();
        
        ScoringRule rule2 = ScoringRule.builder()
                .id(2L)
                .name("Home Improvement Purpose")
                .riskPoints(5)
                .build();
        
        List<ScoringRule> triggeredRules = Arrays.asList(rule1, rule2);
        
        // Test risk score calculation
        int riskScore = ruleEngine.calculateRiskScore(triggeredRules);
        
        // Verify result
        assertEquals(15, riskScore);
    }

    @Test
    void testGenerateExplanation() {
        // Create test rules
        ScoringRule rule1 = ScoringRule.builder()
                .id(1L)
                .name("High Credit Score")
                .riskPoints(10)
                .build();
        
        ScoringRule rule2 = ScoringRule.builder()
                .id(2L)
                .name("Home Improvement Purpose")
                .riskPoints(5)
                .build();
        
        List<ScoringRule> triggeredRules = Arrays.asList(rule1, rule2);
        
        // Test explanation generation
        String explanation = ruleEngine.generateExplanation(triggeredRules);
        
        // Verify result
        assertNotNull(explanation);
        assertTrue(explanation.contains("High Credit Score"));
        assertTrue(explanation.contains("Home Improvement Purpose"));
        assertTrue(explanation.contains("10 points"));
        assertTrue(explanation.contains("5 points"));
        assertTrue(explanation.contains("Total risk score: 15"));
    }
}