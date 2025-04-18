package com.loanrisk.service.impl;

import com.loanrisk.engine.RuleEngine;
import com.loanrisk.engine.calculator.DerivedFieldCalculator;
import com.loanrisk.engine.determiner.RiskLevelDeterminer;
import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;
import com.loanrisk.model.entity.ScoringRule;
import com.loanrisk.service.LoanEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the LoanEvaluationService interface
 */
@Service
public class LoanEvaluationServiceImpl implements LoanEvaluationService {

    private final RuleEngine ruleEngine;
    private final DerivedFieldCalculator derivedFieldCalculator;
    private final RiskLevelDeterminer riskLevelDeterminer;

    @Autowired
    public LoanEvaluationServiceImpl(
            RuleEngine ruleEngine,
            DerivedFieldCalculator derivedFieldCalculator,
            RiskLevelDeterminer riskLevelDeterminer) {
        this.ruleEngine = ruleEngine;
        this.derivedFieldCalculator = derivedFieldCalculator;
        this.riskLevelDeterminer = riskLevelDeterminer;
    }

    @Override
    @Transactional
    public LoanApplication evaluateLoanApplication(LoanApplication loanApplication) {
        Customer customer = loanApplication.getCustomer();
        
        // Calculate derived fields
        Map<String, Object> derivedFields = getDerivedFields(customer, loanApplication);
        
        // Evaluate rules
        List<ScoringRule> triggeredRules = ruleEngine.evaluateRules(loanApplication, customer, derivedFields);
        
        // Calculate risk score
        int riskScore = ruleEngine.calculateRiskScore(triggeredRules);
        
        // Determine risk level
        String riskLevel = riskLevelDeterminer.determineRiskLevel(riskScore);
        
        // Determine decision
        String decision = riskLevelDeterminer.determineDecision(riskLevel);
        
        // Generate explanation
        String explanation = generateExplanation(loanApplication, triggeredRules, derivedFields);
        
        // Update loan application
        loanApplication.setRiskScore(riskScore);
        loanApplication.setRiskLevel(riskLevel);
        loanApplication.setDecision(decision);
        loanApplication.setExplanation(explanation);
        
        return loanApplication;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoringRule> getTriggeredRules(LoanApplication loanApplication) {
        Customer customer = loanApplication.getCustomer();
        Map<String, Object> derivedFields = getDerivedFields(customer, loanApplication);
        return ruleEngine.evaluateRules(loanApplication, customer, derivedFields);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getDerivedFields(Customer customer, LoanApplication loanApplication) {
        return derivedFieldCalculator.calculateDerivedFields(customer, loanApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public String generateExplanation(LoanApplication loanApplication, List<ScoringRule> triggeredRules, Map<String, Object> derivedFields) {
        StringBuilder explanation = new StringBuilder();
        
        // Add basic loan information
        explanation.append("Loan Application Evaluation\n");
        explanation.append("==========================\n\n");
        explanation.append("Loan Amount: $").append(loanApplication.getLoanAmount()).append("\n");
        explanation.append("Loan Purpose: ").append(loanApplication.getLoanPurpose()).append("\n");
        explanation.append("Requested Term: ").append(loanApplication.getRequestedTermMonths()).append(" months\n\n");
        
        // Add customer information
        Customer customer = loanApplication.getCustomer();
        explanation.append("Customer Information\n");
        explanation.append("--------------------\n");
        explanation.append("Credit Score: ").append(customer.getCreditScore()).append("\n");
        explanation.append("Annual Income: $").append(customer.getAnnualIncome()).append("\n");
        explanation.append("Existing Debt: $").append(customer.getExistingDebt()).append("\n");
        explanation.append("Employment Status: ").append(customer.getEmploymentStatus()).append("\n\n");
        
        // Add derived fields
        explanation.append("Derived Fields\n");
        explanation.append("-------------\n");
        for (Map.Entry<String, Object> entry : derivedFields.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            
            // Format the field name for better readability
            String formattedName = formatFieldName(fieldName);
            
            // Format the field value
            String formattedValue = formatFieldValue(fieldValue);
            
            explanation.append(formattedName).append(": ").append(formattedValue).append("\n");
        }
        explanation.append("\n");
        
        // Add triggered rules
        explanation.append("Triggered Risk Factors\n");
        explanation.append("---------------------\n");
        if (triggeredRules.isEmpty()) {
            explanation.append("No risk factors were identified.\n");
        } else {
            for (ScoringRule rule : triggeredRules) {
                explanation.append("- ").append(rule.getName())
                        .append(" (").append(rule.getRiskPoints()).append(" points)\n");
            }
        }
        explanation.append("\n");
        
        // Add risk assessment
        int riskScore = ruleEngine.calculateRiskScore(triggeredRules);
        String riskLevel = riskLevelDeterminer.determineRiskLevel(riskScore);
        String decision = riskLevelDeterminer.determineDecision(riskLevel);
        
        explanation.append("Risk Assessment\n");
        explanation.append("---------------\n");
        explanation.append("Total Risk Score: ").append(riskScore).append("\n");
        explanation.append("Risk Level: ").append(riskLevel).append("\n");
        explanation.append("Decision: ").append(decision).append("\n");
        
        return explanation.toString();
    }
    
    /**
     * Format a field name for better readability
     */
    private String formatFieldName(String fieldName) {
        // Convert camelCase to Title Case with Spaces
        String result = fieldName.replaceAll("([a-z])([A-Z])", "$1 $2");
        return Character.toUpperCase(result.charAt(0)) + result.substring(1);
    }
    
    /**
     * Format a field value for better readability
     */
    private String formatFieldValue(Object value) {
        if (value instanceof BigDecimal) {
            // Format BigDecimal as currency or percentage based on field name
            return ((BigDecimal) value).toPlainString();
        }
        return value.toString();
    }
}