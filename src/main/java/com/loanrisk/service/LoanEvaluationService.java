package com.loanrisk.service;

import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;
import com.loanrisk.model.entity.ScoringRule;

import java.util.List;
import java.util.Map;

/**
 * Service interface for evaluating loan applications
 */
public interface LoanEvaluationService {
    
    /**
     * Evaluate a loan application and determine risk score, risk level, and decision
     * 
     * @param loanApplication the loan application to evaluate
     * @return the updated loan application with risk score, risk level, and decision
     */
    LoanApplication evaluateLoanApplication(LoanApplication loanApplication);
    
    /**
     * Get the triggered rules for a loan application
     * 
     * @param loanApplication the loan application
     * @return list of triggered rules
     */
    List<ScoringRule> getTriggeredRules(LoanApplication loanApplication);
    
    /**
     * Get the derived fields calculated for a loan application
     * 
     * @param customer the customer
     * @param loanApplication the loan application
     * @return map of derived field names to their calculated values
     */
    Map<String, Object> getDerivedFields(Customer customer, LoanApplication loanApplication);
    
    /**
     * Generate an explanation for the loan application evaluation
     * 
     * @param loanApplication the loan application
     * @param triggeredRules the rules that were triggered
     * @param derivedFields the derived fields that were calculated
     * @return explanation string
     */
    String generateExplanation(LoanApplication loanApplication, List<ScoringRule> triggeredRules, Map<String, Object> derivedFields);
}