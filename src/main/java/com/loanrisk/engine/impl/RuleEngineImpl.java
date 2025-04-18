package com.loanrisk.engine.impl;

import com.loanrisk.engine.RuleEngine;
import com.loanrisk.engine.RuleEvaluator;
import com.loanrisk.engine.factory.RuleEvaluatorFactory;
import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;
import com.loanrisk.model.entity.ScoringRule;
import com.loanrisk.repository.ScoringRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the RuleEngine interface
 */
@Component
public class RuleEngineImpl implements RuleEngine {

    private final ScoringRuleRepository scoringRuleRepository;
    private final RuleEvaluatorFactory ruleEvaluatorFactory;

    @Autowired
    public RuleEngineImpl(ScoringRuleRepository scoringRuleRepository, RuleEvaluatorFactory ruleEvaluatorFactory) {
        this.scoringRuleRepository = scoringRuleRepository;
        this.ruleEvaluatorFactory = ruleEvaluatorFactory;
    }

    @Override
    public List<ScoringRule> evaluateRules(LoanApplication loanApplication, Customer customer, Map<String, Object> derivedFields) {
        // Get all enabled rules ordered by priority
        List<ScoringRule> enabledRules = scoringRuleRepository.findByEnabledTrueOrderByPriorityAsc();
        List<ScoringRule> triggeredRules = new ArrayList<>();
        
        for (ScoringRule rule : enabledRules) {
            // Get the appropriate evaluator for this rule's operator
            RuleEvaluator evaluator = ruleEvaluatorFactory.getEvaluator(rule.getOperator());
            
            if (evaluator == null) {
                continue; // Skip rules with unknown operators
            }
            
            // Get the field value to evaluate
            Object fieldValue = getFieldValue(rule.getField(), loanApplication, customer, derivedFields);
            
            if (fieldValue == null) {
                continue; // Skip rules with unknown fields
            }
            
            // Evaluate the rule
            boolean ruleTriggered = evaluator.evaluate(fieldValue, rule.getRuleValue());
            
            if (ruleTriggered) {
                triggeredRules.add(rule);
            }
        }
        
        return triggeredRules;
    }

    @Override
    public int calculateRiskScore(List<ScoringRule> triggeredRules) {
        return triggeredRules.stream()
                .mapToInt(ScoringRule::getRiskPoints)
                .sum();
    }

    @Override
    public String generateExplanation(List<ScoringRule> triggeredRules) {
        if (triggeredRules.isEmpty()) {
            return "No risk factors were identified.";
        }
        
        StringBuilder explanation = new StringBuilder("Risk factors identified:\n");
        
        for (ScoringRule rule : triggeredRules) {
            explanation.append("- ")
                    .append(rule.getName())
                    .append(" (")
                    .append(rule.getRiskPoints())
                    .append(" points)\n");
        }
        
        explanation.append("\nTotal risk score: ")
                .append(calculateRiskScore(triggeredRules));
        
        return explanation.toString();
    }

    /**
     * Get the value of a field from the loan application, customer, or derived fields
     */
    private Object getFieldValue(String fieldName, LoanApplication loanApplication, Customer customer, Map<String, Object> derivedFields) {
        // First check derived fields
        if (derivedFields.containsKey(fieldName)) {
            return derivedFields.get(fieldName);
        }
        
        // Then check customer fields
        switch (fieldName) {
            case "creditScore":
                return customer.getCreditScore();
            case "age":
                return customer.getAge();
            case "annualIncome":
                return customer.getAnnualIncome();
            case "existingDebt":
                return customer.getExistingDebt();
            case "employmentStatus":
                return customer.getEmploymentStatus();
        }
        
        // Then check loan application fields
        switch (fieldName) {
            case "loanAmount":
                return loanApplication.getLoanAmount();
            case "loanPurpose":
                return loanApplication.getLoanPurpose();
            case "requestedTermMonths":
                return loanApplication.getRequestedTermMonths();
        }
        
        return null; // Field not found
    }
}