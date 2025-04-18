package com.loanrisk.service;

import com.loanrisk.model.dto.LoanApplicationRequestDto;
import com.loanrisk.model.dto.LoanApplicationResponseDto;
import com.loanrisk.model.entity.Customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for managing loan application operations
 */
public interface LoanApplicationService {
    
    /**
     * Create a new loan application
     * 
     * @param loanApplicationRequestDto the loan application data
     * @return the created loan application
     */
    LoanApplicationResponseDto createLoanApplication(LoanApplicationRequestDto loanApplicationRequestDto);
    
    /**
     * Get a loan application by ID
     * 
     * @param id the loan application ID
     * @return the loan application
     */
    LoanApplicationResponseDto getLoanApplicationById(Long id);
    
    /**
     * Get all loan applications
     * 
     * @return list of all loan applications
     */
    List<LoanApplicationResponseDto> getAllLoanApplications();
    
    /**
     * Update a loan application
     * 
     * @param id the loan application ID
     * @param loanApplicationRequestDto the updated loan application data
     * @return the updated loan application
     */
    LoanApplicationResponseDto updateLoanApplication(Long id, LoanApplicationRequestDto loanApplicationRequestDto);
    
    /**
     * Delete a loan application
     * 
     * @param id the loan application ID
     */
    void deleteLoanApplication(Long id);
    
    /**
     * Find loan applications by customer
     * 
     * @param customer the customer
     * @return list of matching loan applications
     */
    List<LoanApplicationResponseDto> findLoanApplicationsByCustomer(Customer customer);
    
    /**
     * Find loan applications by customer ID
     * 
     * @param customerId the customer ID
     * @return list of matching loan applications
     */
    List<LoanApplicationResponseDto> findLoanApplicationsByCustomerId(Long customerId);
    
    /**
     * Find loan applications by risk level
     * 
     * @param riskLevel the risk level
     * @return list of matching loan applications
     */
    List<LoanApplicationResponseDto> findLoanApplicationsByRiskLevel(String riskLevel);
    
    /**
     * Find loan applications by decision
     * 
     * @param decision the decision
     * @return list of matching loan applications
     */
    List<LoanApplicationResponseDto> findLoanApplicationsByDecision(String decision);
    
    /**
     * Find loan applications by loan purpose (case-insensitive partial match)
     * 
     * @param loanPurpose the loan purpose to search for
     * @return list of matching loan applications
     */
    List<LoanApplicationResponseDto> findLoanApplicationsByLoanPurpose(String loanPurpose);
    
    /**
     * Find loan applications with loan amount between min and max
     * 
     * @param minAmount the minimum loan amount
     * @param maxAmount the maximum loan amount
     * @return list of matching loan applications
     */
    List<LoanApplicationResponseDto> findLoanApplicationsByLoanAmountRange(BigDecimal minAmount, BigDecimal maxAmount);
    
    /**
     * Find loan applications with risk score greater than or equal to specified value
     * 
     * @param minRiskScore the minimum risk score
     * @return list of matching loan applications
     */
    List<LoanApplicationResponseDto> findLoanApplicationsByMinimumRiskScore(Integer minRiskScore);
    
    /**
     * Find loan applications created between start and end dates
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return list of matching loan applications
     */
    List<LoanApplicationResponseDto> findLoanApplicationsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find high risk high value applications
     * 
     * @param minRiskScore the minimum risk score
     * @param minAmount the minimum loan amount
     * @return list of matching loan applications
     */
    List<LoanApplicationResponseDto> findHighRiskHighValueApplications(Integer minRiskScore, BigDecimal minAmount);
    
    /**
     * Evaluate a loan application and calculate risk score
     * 
     * @param id the loan application ID
     * @return the evaluated loan application
     */
    LoanApplicationResponseDto evaluateLoanApplication(Long id);
}