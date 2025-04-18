package com.loanrisk.repository;

import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    
    // Find loan applications by customer
    List<LoanApplication> findByCustomer(Customer customer);
    
    // Find loan applications by customer ID
    List<LoanApplication> findByCustomerId(Long customerId);
    
    // Find loan applications by risk level
    List<LoanApplication> findByRiskLevel(String riskLevel);
    
    // Find loan applications by decision
    List<LoanApplication> findByDecision(String decision);
    
    // Find loan applications by loan purpose (case-insensitive partial match)
    List<LoanApplication> findByLoanPurposeContainingIgnoreCase(String loanPurpose);
    
    // Find loan applications with loan amount between min and max
    List<LoanApplication> findByLoanAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    
    // Find loan applications with risk score greater than or equal to specified value
    List<LoanApplication> findByRiskScoreGreaterThanEqual(Integer minRiskScore);
    
    // Find loan applications created between start and end dates
    List<LoanApplication> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Custom query to find applications with high risk score and large loan amounts
    @Query("SELECT la FROM LoanApplication la WHERE la.riskScore >= :minRiskScore AND la.loanAmount >= :minAmount")
    List<LoanApplication> findHighRiskHighValueApplications(
            @Param("minRiskScore") Integer minRiskScore, 
            @Param("minAmount") BigDecimal minAmount);
}