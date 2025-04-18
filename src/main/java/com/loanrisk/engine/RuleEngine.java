package com.loanrisk.engine;

import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;
import com.loanrisk.model.entity.ScoringRule;

import java.util.List;
import java.util.Map;

/**
 * Interface for the rule engine that evaluates loan applications against scoring rules
 */
public interface RuleEngine {
    
    /**
     * Evaluate a loan application against all enabled rules
     * 
     * @param loanApplication the loan application to evaluate
     * @param customer the customer associated with the loan application
     * @param derivedFields map of derived fields calculated for this evaluation
     * @return list of rules that were triggered
     */
    List<ScoringRule> evaluateRules(LoanApplication loanApplication, Customer customer, Map<String, Object> derivedFields);
    
    /**
     * Calculate the total risk score based on triggered rules
     * 
     * @param triggeredRules list of rules that were triggered
     * @return the total risk score
     */
    int calculateRiskScore(List<ScoringRule> triggeredRules);
    
    /**
     * Get explanation for why rules were triggered
     * 
     * @param triggeredRules list of rules that were triggered
     * @return explanation string
     */
    String generateExplanation(List<ScoringRule> triggeredRules);
}