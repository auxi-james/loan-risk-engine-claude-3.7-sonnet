package com.loanrisk.engine.calculator;

import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;

import java.util.Map;

/**
 * Interface for calculating derived fields based on customer and loan application data
 */
public interface DerivedFieldCalculator {
    
    /**
     * Calculate derived fields based on customer and loan application data
     * 
     * @param customer the customer data
     * @param loanApplication the loan application data
     * @return map of derived field names to their calculated values
     */
    Map<String, Object> calculateDerivedFields(Customer customer, LoanApplication loanApplication);
    
    /**
     * Get the names of all derived fields that this calculator can compute
     * 
     * @return array of derived field names
     */
    String[] getDerivedFieldNames();
}