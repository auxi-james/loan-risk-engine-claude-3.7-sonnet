package com.loanrisk.engine.calculator;

import com.loanrisk.engine.calculator.impl.DefaultDerivedFieldCalculator;
import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DerivedFieldCalculatorTest {

    private DerivedFieldCalculator calculator;
    private Customer customer;
    private LoanApplication loanApplication;

    @BeforeEach
    void setUp() {
        calculator = new DefaultDerivedFieldCalculator();
        
        // Create test customer
        customer = Customer.builder()
                .name("John Doe")
                .age(35)
                .annualIncome(new BigDecimal("60000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("1000.00"))
                .build();
        
        // Create test loan application
        loanApplication = LoanApplication.builder()
                .customer(customer)
                .loanAmount(new BigDecimal("20000.00"))
                .loanPurpose("HOME_IMPROVEMENT")
                .requestedTermMonths(36)
                .build();
    }

    @Test
    void testCalculateDerivedFields() {
        Map<String, Object> derivedFields = calculator.calculateDerivedFields(customer, loanApplication);
        
        // Verify all expected fields are present
        String[] expectedFields = calculator.getDerivedFieldNames();
        for (String field : expectedFields) {
            assertTrue(derivedFields.containsKey(field), "Derived field " + field + " should be present");
            assertNotNull(derivedFields.get(field), "Derived field " + field + " should not be null");
        }
        
        // Verify debtToIncomeRatio calculation
        BigDecimal monthlyIncome = customer.getAnnualIncome().divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        BigDecimal expectedDebtToIncomeRatio = customer.getExistingDebt().divide(monthlyIncome, 2, RoundingMode.HALF_UP);
        assertEquals(expectedDebtToIncomeRatio, derivedFields.get(DefaultDerivedFieldCalculator.DEBT_TO_INCOME_RATIO));
        
        // Verify loanToIncomeRatio calculation
        BigDecimal expectedLoanToIncomeRatio = loanApplication.getLoanAmount().divide(customer.getAnnualIncome(), 2, RoundingMode.HALF_UP);
        assertEquals(expectedLoanToIncomeRatio, derivedFields.get(DefaultDerivedFieldCalculator.LOAN_TO_INCOME_RATIO));
        
        // Verify loanAmountPerTerm calculation
        BigDecimal expectedLoanAmountPerTerm = loanApplication.getLoanAmount()
                .divide(new BigDecimal(loanApplication.getRequestedTermMonths()), 2, RoundingMode.HALF_UP);
        assertEquals(expectedLoanAmountPerTerm, derivedFields.get(DefaultDerivedFieldCalculator.LOAN_AMOUNT_PER_TERM));
        
        // Verify ageToTermRatio calculation
        BigDecimal termInYears = new BigDecimal(loanApplication.getRequestedTermMonths()).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        BigDecimal expectedAgeToTermRatio = new BigDecimal(customer.getAge()).divide(termInYears, 2, RoundingMode.HALF_UP);
        assertEquals(expectedAgeToTermRatio, derivedFields.get(DefaultDerivedFieldCalculator.AGE_TO_TERM_RATIO));
        
        // Verify monthlyLoanPayment is a reasonable value (just check it's positive and not zero)
        BigDecimal monthlyLoanPayment = (BigDecimal) derivedFields.get(DefaultDerivedFieldCalculator.MONTHLY_LOAN_PAYMENT);
        assertTrue(monthlyLoanPayment.compareTo(BigDecimal.ZERO) > 0);
        
        // Verify totalDebtRatio is greater than debtToIncomeRatio (since it includes the loan payment)
        BigDecimal totalDebtRatio = (BigDecimal) derivedFields.get(DefaultDerivedFieldCalculator.TOTAL_DEBT_RATIO);
        BigDecimal debtToIncomeRatio = (BigDecimal) derivedFields.get(DefaultDerivedFieldCalculator.DEBT_TO_INCOME_RATIO);
        assertTrue(totalDebtRatio.compareTo(debtToIncomeRatio) > 0);
    }

    @Test
    void testGetDerivedFieldNames() {
        String[] fieldNames = calculator.getDerivedFieldNames();
        
        assertNotNull(fieldNames);
        assertTrue(fieldNames.length > 0);
        
        // Verify specific field names are included
        boolean hasDebtToIncomeRatio = false;
        boolean hasLoanToIncomeRatio = false;
        
        for (String fieldName : fieldNames) {
            if (fieldName.equals(DefaultDerivedFieldCalculator.DEBT_TO_INCOME_RATIO)) {
                hasDebtToIncomeRatio = true;
            } else if (fieldName.equals(DefaultDerivedFieldCalculator.LOAN_TO_INCOME_RATIO)) {
                hasLoanToIncomeRatio = true;
            }
        }
        
        assertTrue(hasDebtToIncomeRatio, "Field names should include debtToIncomeRatio");
        assertTrue(hasLoanToIncomeRatio, "Field names should include loanToIncomeRatio");
    }
}