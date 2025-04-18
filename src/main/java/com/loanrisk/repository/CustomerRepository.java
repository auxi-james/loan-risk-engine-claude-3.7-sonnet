package com.loanrisk.repository;

import com.loanrisk.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Find customers by name (case-insensitive partial match)
    List<Customer> findByNameContainingIgnoreCase(String name);
    
    // Find customers by credit score range
    List<Customer> findByCreditScoreBetween(Integer minScore, Integer maxScore);
    
    // Find customers by employment status
    List<Customer> findByEmploymentStatus(String employmentStatus);
    
    // Find customers with annual income greater than or equal to specified amount
    List<Customer> findByAnnualIncomeGreaterThanEqual(BigDecimal minIncome);
    
    // Find customers with existing debt less than specified amount
    List<Customer> findByExistingDebtLessThan(BigDecimal maxDebt);
    
    // Find customers by age range
    List<Customer> findByAgeBetween(Integer minAge, Integer maxAge);
}