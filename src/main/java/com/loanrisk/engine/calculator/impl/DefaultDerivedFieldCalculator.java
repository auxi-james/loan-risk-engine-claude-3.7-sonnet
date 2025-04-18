package com.loanrisk.engine.calculator.impl;

import com.loanrisk.engine.calculator.DerivedFieldCalculator;
import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the DerivedFieldCalculator interface
 */
@Component
public class DefaultDerivedFieldCalculator implements DerivedFieldCalculator {

    // Constants for derived field names
    public static final String DEBT_TO_INCOME_RATIO = "debtToIncomeRatio";
    public static final String LOAN_TO_INCOME_RATIO = "loanToIncomeRatio";
    public static final String MONTHLY_LOAN_PAYMENT = "monthlyLoanPayment";
    public static final String TOTAL_DEBT_RATIO = "totalDebtRatio";
    public static final String LOAN_AMOUNT_PER_TERM = "loanAmountPerTerm";
    public static final String AGE_TO_TERM_RATIO = "ageToTermRatio";

    // Array of all derived field names
    private static final String[] DERIVED_FIELD_NAMES = {
            DEBT_TO_INCOME_RATIO,
            LOAN_TO_INCOME_RATIO,
            MONTHLY_LOAN_PAYMENT,
            TOTAL_DEBT_RATIO,
            LOAN_AMOUNT_PER_TERM,
            AGE_TO_TERM_RATIO
    };

    // Annual interest rate used for monthly payment calculation (5.5%)
    private static final BigDecimal ANNUAL_INTEREST_RATE = new BigDecimal("0.055");
    
    @Override
    public Map<String, Object> calculateDerivedFields(Customer customer, LoanApplication loanApplication) {
        Map<String, Object> derivedFields = new HashMap<>();
        
        // Calculate monthly income (annual income / 12)
        BigDecimal monthlyIncome = customer.getAnnualIncome().divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        
        // Calculate debt to income ratio (existing debt / monthly income)
        BigDecimal debtToIncomeRatio = customer.getExistingDebt().divide(monthlyIncome, 2, RoundingMode.HALF_UP);
        derivedFields.put(DEBT_TO_INCOME_RATIO, debtToIncomeRatio);
        
        // Calculate loan to income ratio (loan amount / annual income)
        BigDecimal loanToIncomeRatio = loanApplication.getLoanAmount().divide(customer.getAnnualIncome(), 2, RoundingMode.HALF_UP);
        derivedFields.put(LOAN_TO_INCOME_RATIO, loanToIncomeRatio);
        
        // Calculate monthly loan payment
        BigDecimal monthlyLoanPayment = calculateMonthlyPayment(
                loanApplication.getLoanAmount(), 
                ANNUAL_INTEREST_RATE, 
                loanApplication.getRequestedTermMonths());
        derivedFields.put(MONTHLY_LOAN_PAYMENT, monthlyLoanPayment);
        
        // Calculate total debt ratio ((existing debt + monthly loan payment) / monthly income)
        BigDecimal totalDebtRatio = customer.getExistingDebt().add(monthlyLoanPayment)
                .divide(monthlyIncome, 2, RoundingMode.HALF_UP);
        derivedFields.put(TOTAL_DEBT_RATIO, totalDebtRatio);
        
        // Calculate loan amount per term (loan amount / term months)
        BigDecimal loanAmountPerTerm = loanApplication.getLoanAmount()
                .divide(new BigDecimal(loanApplication.getRequestedTermMonths()), 2, RoundingMode.HALF_UP);
        derivedFields.put(LOAN_AMOUNT_PER_TERM, loanAmountPerTerm);
        
        // Calculate age to term ratio (customer age / term in years)
        BigDecimal termInYears = new BigDecimal(loanApplication.getRequestedTermMonths()).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        BigDecimal ageToTermRatio = new BigDecimal(customer.getAge()).divide(termInYears, 2, RoundingMode.HALF_UP);
        derivedFields.put(AGE_TO_TERM_RATIO, ageToTermRatio);
        
        return derivedFields;
    }

    @Override
    public String[] getDerivedFieldNames() {
        return DERIVED_FIELD_NAMES;
    }
    
    /**
     * Calculate the monthly payment for a loan
     * 
     * @param principal the loan amount
     * @param annualInterestRate the annual interest rate (e.g., 0.055 for 5.5%)
     * @param termMonths the loan term in months
     * @return the monthly payment amount
     */
    private BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal annualInterestRate, int termMonths) {
        // Monthly interest rate = annual rate / 12
        BigDecimal monthlyRate = annualInterestRate.divide(new BigDecimal("12"), 8, RoundingMode.HALF_UP);
        
        // Calculate (1 + r)^n
        BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal compoundFactor = onePlusRate.pow(termMonths);
        
        // Calculate monthly payment: P * r * (1 + r)^n / ((1 + r)^n - 1)
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(compoundFactor);
        BigDecimal denominator = compoundFactor.subtract(BigDecimal.ONE);
        
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}