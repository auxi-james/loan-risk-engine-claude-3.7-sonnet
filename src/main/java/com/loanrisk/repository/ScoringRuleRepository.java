package com.loanrisk.repository;

import com.loanrisk.model.entity.ScoringRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoringRuleRepository extends JpaRepository<ScoringRule, Long> {
    
    // Find all enabled rules
    List<ScoringRule> findByEnabledTrue();
    
    // Find all disabled rules
    List<ScoringRule> findByEnabledFalse();
    
    // Find rules by field
    List<ScoringRule> findByField(String field);
    
    // Find rules by priority ordered by priority ascending
    List<ScoringRule> findByPriorityLessThanEqualOrderByPriorityAsc(Integer maxPriority);
    
    // Find rules by priority range ordered by priority
    List<ScoringRule> findByPriorityBetweenOrderByPriorityAsc(Integer minPriority, Integer maxPriority);
    
    // Find enabled rules by field
    List<ScoringRule> findByFieldAndEnabledTrue(String field);
    
    // Find enabled rules ordered by priority
    List<ScoringRule> findByEnabledTrueOrderByPriorityAsc();
    
    // Find rules by name containing (case-insensitive)
    List<ScoringRule> findByNameContainingIgnoreCase(String name);
    
    // Find rules by operator
    List<ScoringRule> findByOperator(String operator);
    
    // Find rules with risk points greater than or equal to specified value
    List<ScoringRule> findByRiskPointsGreaterThanEqual(Integer minRiskPoints);
    
    // Custom query to find high priority enabled rules
    @Query("SELECT sr FROM ScoringRule sr WHERE sr.enabled = true AND sr.priority <= :maxPriority ORDER BY sr.priority ASC")
    List<ScoringRule> findHighPriorityEnabledRules(Integer maxPriority);
}