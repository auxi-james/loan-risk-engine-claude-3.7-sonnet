package com.loanrisk.service.impl;

import com.loanrisk.exception.ResourceNotFoundException;
import com.loanrisk.model.dto.ScoringRuleResponseDto;
import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;
import com.loanrisk.model.entity.ScoringRule;
import com.loanrisk.repository.ScoringRuleRepository;
import com.loanrisk.service.ScoringRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScoringRuleServiceImpl implements ScoringRuleService {

    private final ScoringRuleRepository scoringRuleRepository;

    @Autowired
    public ScoringRuleServiceImpl(ScoringRuleRepository scoringRuleRepository) {
        this.scoringRuleRepository = scoringRuleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ScoringRuleResponseDto getScoringRuleById(Long id) {
        ScoringRule scoringRule = scoringRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ScoringRule", "id", id));
        
        return mapToDto(scoringRule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoringRuleResponseDto> getAllScoringRules() {
        List<ScoringRule> scoringRules = scoringRuleRepository.findAll();
        
        return scoringRules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoringRuleResponseDto> findEnabledRules() {
        List<ScoringRule> scoringRules = scoringRuleRepository.findByEnabledTrue();
        
        return scoringRules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoringRuleResponseDto> findDisabledRules() {
        List<ScoringRule> scoringRules = scoringRuleRepository.findByEnabledFalse();
        
        return scoringRules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoringRuleResponseDto> findRulesByField(String field) {
        List<ScoringRule> scoringRules = scoringRuleRepository.findByField(field);
        
        return scoringRules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoringRuleResponseDto> findRulesByMaxPriority(Integer maxPriority) {
        List<ScoringRule> scoringRules = scoringRuleRepository.findByPriorityLessThanEqualOrderByPriorityAsc(maxPriority);
        
        return scoringRules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoringRuleResponseDto> findRulesByPriorityRange(Integer minPriority, Integer maxPriority) {
        List<ScoringRule> scoringRules = scoringRuleRepository.findByPriorityBetweenOrderByPriorityAsc(minPriority, maxPriority);
        
        return scoringRules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoringRuleResponseDto> findEnabledRulesByField(String field) {
        List<ScoringRule> scoringRules = scoringRuleRepository.findByFieldAndEnabledTrue(field);
        
        return scoringRules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoringRuleResponseDto> findEnabledRulesOrderedByPriority() {
        List<ScoringRule> scoringRules = scoringRuleRepository.findByEnabledTrueOrderByPriorityAsc();
        
        return scoringRules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoringRuleResponseDto> findRulesByNameContaining(String name) {
        List<ScoringRule> scoringRules = scoringRuleRepository.findByNameContainingIgnoreCase(name);
        
        return scoringRules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoringRuleResponseDto> findRulesByOperator(String operator) {
        List<ScoringRule> scoringRules = scoringRuleRepository.findByOperator(operator);
        
        return scoringRules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoringRuleResponseDto> findRulesByMinimumRiskPoints(Integer minRiskPoints) {
        List<ScoringRule> scoringRules = scoringRuleRepository.findByRiskPointsGreaterThanEqual(minRiskPoints);
        
        return scoringRules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoringRuleResponseDto> findHighPriorityEnabledRules(Integer maxPriority) {
        List<ScoringRule> scoringRules = scoringRuleRepository.findHighPriorityEnabledRules(maxPriority);
        
        return scoringRules.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ScoringRuleResponseDto enableRule(Long id) {
        ScoringRule scoringRule = scoringRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ScoringRule", "id", id));
        
        scoringRule.setEnabled(true);
        ScoringRule updatedRule = scoringRuleRepository.save(scoringRule);
        
        return mapToDto(updatedRule);
    }

    @Override
    @Transactional
    public ScoringRuleResponseDto disableRule(Long id) {
        ScoringRule scoringRule = scoringRuleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ScoringRule", "id", id));
        
        scoringRule.setEnabled(false);
        ScoringRule updatedRule = scoringRuleRepository.save(scoringRule);
        
        return mapToDto(updatedRule);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer applyRules(LoanApplication loanApplication) {
        List<ScoringRule> applicableRules = getApplicableRules(loanApplication);
        
        int totalRiskScore = 0;
        for (ScoringRule rule : applicableRules) {
            totalRiskScore += rule.getRiskPoints();
        }
        
        return totalRiskScore;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScoringRule> getApplicableRules(LoanApplication loanApplication) {
        // Get all enabled rules ordered by priority
        List<ScoringRule> enabledRules = scoringRuleRepository.findByEnabledTrueOrderByPriorityAsc();
        List<ScoringRule> applicableRules = new ArrayList<>();
        
        Customer customer = loanApplication.getCustomer();
        
        for (ScoringRule rule : enabledRules) {
            boolean ruleApplies = false;
            
            switch (rule.getField()) {
                case "creditScore":
                    ruleApplies = evaluateNumericRule(customer.getCreditScore(), rule.getOperator(), rule.getRuleValue());
                    break;
                case "age":
                    ruleApplies = evaluateNumericRule(customer.getAge(), rule.getOperator(), rule.getRuleValue());
                    break;
                case "annualIncome":
                    ruleApplies = evaluateDecimalRule(customer.getAnnualIncome(), rule.getOperator(), rule.getRuleValue());
                    break;
                case "existingDebt":
                    ruleApplies = evaluateDecimalRule(customer.getExistingDebt(), rule.getOperator(), rule.getRuleValue());
                    break;
                case "employmentStatus":
                    ruleApplies = evaluateStringRule(customer.getEmploymentStatus(), rule.getOperator(), rule.getRuleValue());
                    break;
                case "loanAmount":
                    ruleApplies = evaluateDecimalRule(loanApplication.getLoanAmount(), rule.getOperator(), rule.getRuleValue());
                    break;
                case "loanPurpose":
                    ruleApplies = evaluateStringRule(loanApplication.getLoanPurpose(), rule.getOperator(), rule.getRuleValue());
                    break;
                case "requestedTermMonths":
                    ruleApplies = evaluateNumericRule(loanApplication.getRequestedTermMonths(), rule.getOperator(), rule.getRuleValue());
                    break;
                case "debtToIncomeRatio":
                    BigDecimal monthlyIncome = customer.getAnnualIncome().divide(new BigDecimal("12"), 2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal debtToIncomeRatio = customer.getExistingDebt().divide(monthlyIncome, 2, BigDecimal.ROUND_HALF_UP);
                    ruleApplies = evaluateDecimalRule(debtToIncomeRatio, rule.getOperator(), rule.getRuleValue());
                    break;
                case "loanToIncomeRatio":
                    BigDecimal loanToIncomeRatio = loanApplication.getLoanAmount().divide(customer.getAnnualIncome(), 2, BigDecimal.ROUND_HALF_UP);
                    ruleApplies = evaluateDecimalRule(loanToIncomeRatio, rule.getOperator(), rule.getRuleValue());
                    break;
            }
            
            if (ruleApplies) {
                applicableRules.add(rule);
            }
        }
        
        return applicableRules;
    }

    /**
     * Evaluate a numeric rule
     */
    private boolean evaluateNumericRule(Integer value, String operator, String ruleValue) {
        int compareValue = Integer.parseInt(ruleValue);
        
        switch (operator) {
            case "EQUALS":
                return value.equals(compareValue);
            case "NOT_EQUALS":
                return !value.equals(compareValue);
            case "GREATER_THAN":
                return value > compareValue;
            case "GREATER_THAN_OR_EQUAL":
                return value >= compareValue;
            case "LESS_THAN":
                return value < compareValue;
            case "LESS_THAN_OR_EQUAL":
                return value <= compareValue;
            default:
                return false;
        }
    }

    /**
     * Evaluate a decimal rule
     */
    private boolean evaluateDecimalRule(BigDecimal value, String operator, String ruleValue) {
        BigDecimal compareValue = new BigDecimal(ruleValue);
        
        switch (operator) {
            case "EQUALS":
                return value.compareTo(compareValue) == 0;
            case "NOT_EQUALS":
                return value.compareTo(compareValue) != 0;
            case "GREATER_THAN":
                return value.compareTo(compareValue) > 0;
            case "GREATER_THAN_OR_EQUAL":
                return value.compareTo(compareValue) >= 0;
            case "LESS_THAN":
                return value.compareTo(compareValue) < 0;
            case "LESS_THAN_OR_EQUAL":
                return value.compareTo(compareValue) <= 0;
            default:
                return false;
        }
    }

    /**
     * Evaluate a string rule
     */
    private boolean evaluateStringRule(String value, String operator, String ruleValue) {
        switch (operator) {
            case "EQUALS":
                return value.equals(ruleValue);
            case "NOT_EQUALS":
                return !value.equals(ruleValue);
            case "CONTAINS":
                return value.contains(ruleValue);
            case "NOT_CONTAINS":
                return !value.contains(ruleValue);
            case "STARTS_WITH":
                return value.startsWith(ruleValue);
            case "ENDS_WITH":
                return value.endsWith(ruleValue);
            default:
                return false;
        }
    }

    /**
     * Map ScoringRule entity to ScoringRuleResponseDto
     */
    private ScoringRuleResponseDto mapToDto(ScoringRule scoringRule) {
        return ScoringRuleResponseDto.builder()
                .id(scoringRule.getId())
                .name(scoringRule.getName())
                .field(scoringRule.getField())
                .operator(scoringRule.getOperator())
                .ruleValue(scoringRule.getRuleValue())
                .riskPoints(scoringRule.getRiskPoints())
                .priority(scoringRule.getPriority())
                .enabled(scoringRule.getEnabled())
                .createdAt(scoringRule.getCreatedAt())
                .updatedAt(scoringRule.getUpdatedAt())
                .build();
    }
}