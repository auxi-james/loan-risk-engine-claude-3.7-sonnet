package com.loanrisk.service;

import com.loanrisk.model.dto.ScoringRuleResponseDto;
import com.loanrisk.model.entity.LoanApplication;
import com.loanrisk.model.entity.ScoringRule;

import java.util.List;

/**
 * Service interface for managing scoring rule operations
 */
public interface ScoringRuleService {
    
    /**
     * Get a scoring rule by ID
     * 
     * @param id the scoring rule ID
     * @return the scoring rule
     */
    ScoringRuleResponseDto getScoringRuleById(Long id);
    
    /**
     * Get all scoring rules
     * 
     * @return list of all scoring rules
     */
    List<ScoringRuleResponseDto> getAllScoringRules();
    
    /**
     * Find all enabled rules
     * 
     * @return list of enabled rules
     */
    List<ScoringRuleResponseDto> findEnabledRules();
    
    /**
     * Find all disabled rules
     * 
     * @return list of disabled rules
     */
    List<ScoringRuleResponseDto> findDisabledRules();
    
    /**
     * Find rules by field
     * 
     * @param field the field to search for
     * @return list of matching rules
     */
    List<ScoringRuleResponseDto> findRulesByField(String field);
    
    /**
     * Find rules by priority less than or equal to max priority, ordered by priority ascending
     * 
     * @param maxPriority the maximum priority
     * @return list of matching rules
     */
    List<ScoringRuleResponseDto> findRulesByMaxPriority(Integer maxPriority);
    
    /**
     * Find rules by priority range ordered by priority
     * 
     * @param minPriority the minimum priority
     * @param maxPriority the maximum priority
     * @return list of matching rules
     */
    List<ScoringRuleResponseDto> findRulesByPriorityRange(Integer minPriority, Integer maxPriority);
    
    /**
     * Find enabled rules by field
     * 
     * @param field the field to search for
     * @return list of matching enabled rules
     */
    List<ScoringRuleResponseDto> findEnabledRulesByField(String field);
    
    /**
     * Find enabled rules ordered by priority
     * 
     * @return list of enabled rules ordered by priority
     */
    List<ScoringRuleResponseDto> findEnabledRulesOrderedByPriority();
    
    /**
     * Find rules by name containing (case-insensitive)
     * 
     * @param name the name to search for
     * @return list of matching rules
     */
    List<ScoringRuleResponseDto> findRulesByNameContaining(String name);
    
    /**
     * Find rules by operator
     * 
     * @param operator the operator to search for
     * @return list of matching rules
     */
    List<ScoringRuleResponseDto> findRulesByOperator(String operator);
    
    /**
     * Find rules with risk points greater than or equal to specified value
     * 
     * @param minRiskPoints the minimum risk points
     * @return list of matching rules
     */
    List<ScoringRuleResponseDto> findRulesByMinimumRiskPoints(Integer minRiskPoints);
    
    /**
     * Find high priority enabled rules
     * 
     * @param maxPriority the maximum priority
     * @return list of high priority enabled rules
     */
    List<ScoringRuleResponseDto> findHighPriorityEnabledRules(Integer maxPriority);
    
    /**
     * Enable a scoring rule
     * 
     * @param id the scoring rule ID
     * @return the updated scoring rule
     */
    ScoringRuleResponseDto enableRule(Long id);
    
    /**
     * Disable a scoring rule
     * 
     * @param id the scoring rule ID
     * @return the updated scoring rule
     */
    ScoringRuleResponseDto disableRule(Long id);
    
    /**
     * Apply scoring rules to a loan application
     * 
     * @param loanApplication the loan application to evaluate
     * @return the total risk score
     */
    Integer applyRules(LoanApplication loanApplication);
    
    /**
     * Get applicable rules for a loan application
     * 
     * @param loanApplication the loan application
     * @return list of applicable rules
     */
    List<ScoringRule> getApplicableRules(LoanApplication loanApplication);
}