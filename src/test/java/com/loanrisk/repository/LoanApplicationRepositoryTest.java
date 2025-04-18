package com.loanrisk.repository;

import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class LoanApplicationRepositoryTest {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    public void setup() {
        // Create test customers
        customer1 = Customer.builder()
                .name("John Doe")
                .age(35)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .build();

        customer2 = Customer.builder()
                .name("Jane Smith")
                .age(28)
                .annualIncome(new BigDecimal("65000.00"))
                .creditScore(680)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("10000.00"))
                .build();

        customerRepository.saveAll(List.of(customer1, customer2));
    }

    @Test
    public void testSaveLoanApplication() {
        // Create a loan application
        LoanApplication loanApplication = LoanApplication.builder()
                .customer(customer1)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("Home Renovation")
                .requestedTermMonths(36)
                .build();

        // Save the loan application
        LoanApplication savedApplication = loanApplicationRepository.save(loanApplication);

        // Verify the loan application was saved with an ID
        assertThat(savedApplication.getId()).isNotNull();
        assertThat(savedApplication.getLoanPurpose()).isEqualTo("Home Renovation");
        assertThat(savedApplication.getCreatedAt()).isNotNull();
        assertThat(savedApplication.getUpdatedAt()).isNotNull();
    }

    @Test
    public void testFindByCustomer() {
        // Create and save loan applications
        LoanApplication application1 = LoanApplication.builder()
                .customer(customer1)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("Home Renovation")
                .requestedTermMonths(36)
                .build();

        LoanApplication application2 = LoanApplication.builder()
                .customer(customer1)
                .loanAmount(new BigDecimal("10000.00"))
                .loanPurpose("Debt Consolidation")
                .requestedTermMonths(24)
                .build();

        LoanApplication application3 = LoanApplication.builder()
                .customer(customer2)
                .loanAmount(new BigDecimal("15000.00"))
                .loanPurpose("Car Purchase")
                .requestedTermMonths(48)
                .build();

        loanApplicationRepository.saveAll(List.of(application1, application2, application3));

        // Test finding by customer
        List<LoanApplication> customer1Applications = loanApplicationRepository.findByCustomer(customer1);
        assertThat(customer1Applications).hasSize(2);
        assertThat(customer1Applications).extracting(LoanApplication::getLoanPurpose)
                .containsExactlyInAnyOrder("Home Renovation", "Debt Consolidation");

        List<LoanApplication> customer2Applications = loanApplicationRepository.findByCustomer(customer2);
        assertThat(customer2Applications).hasSize(1);
        assertThat(customer2Applications.get(0).getLoanPurpose()).isEqualTo("Car Purchase");
    }

    @Test
    public void testFindByCustomerId() {
        // Create and save loan applications
        LoanApplication application1 = LoanApplication.builder()
                .customer(customer1)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("Home Renovation")
                .requestedTermMonths(36)
                .build();

        LoanApplication application2 = LoanApplication.builder()
                .customer(customer2)
                .loanAmount(new BigDecimal("15000.00"))
                .loanPurpose("Car Purchase")
                .requestedTermMonths(48)
                .build();

        loanApplicationRepository.saveAll(List.of(application1, application2));

        // Test finding by customer ID
        List<LoanApplication> customer1Applications = loanApplicationRepository.findByCustomerId(customer1.getId());
        assertThat(customer1Applications).hasSize(1);
        assertThat(customer1Applications.get(0).getLoanPurpose()).isEqualTo("Home Renovation");
    }

    @Test
    public void testFindByLoanPurposeContainingIgnoreCase() {
        // Create and save loan applications
        LoanApplication application1 = LoanApplication.builder()
                .customer(customer1)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("Home Renovation")
                .requestedTermMonths(36)
                .build();

        LoanApplication application2 = LoanApplication.builder()
                .customer(customer1)
                .loanAmount(new BigDecimal("10000.00"))
                .loanPurpose("Debt Consolidation")
                .requestedTermMonths(24)
                .build();

        LoanApplication application3 = LoanApplication.builder()
                .customer(customer2)
                .loanAmount(new BigDecimal("15000.00"))
                .loanPurpose("Car Purchase")
                .requestedTermMonths(48)
                .build();

        loanApplicationRepository.saveAll(List.of(application1, application2, application3));

        // Test finding by loan purpose containing "home" (case-insensitive)
        List<LoanApplication> homeApplications = loanApplicationRepository.findByLoanPurposeContainingIgnoreCase("home");
        assertThat(homeApplications).hasSize(1);
        assertThat(homeApplications.get(0).getLoanPurpose()).isEqualTo("Home Renovation");

        // Test finding by loan purpose containing "purchase" (case-insensitive)
        List<LoanApplication> purchaseApplications = loanApplicationRepository.findByLoanPurposeContainingIgnoreCase("purchase");
        assertThat(purchaseApplications).hasSize(1);
        assertThat(purchaseApplications.get(0).getLoanPurpose()).isEqualTo("Car Purchase");
    }

    @Test
    public void testFindByLoanAmountBetween() {
        // Create and save loan applications
        LoanApplication application1 = LoanApplication.builder()
                .customer(customer1)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("Home Renovation")
                .requestedTermMonths(36)
                .build();

        LoanApplication application2 = LoanApplication.builder()
                .customer(customer1)
                .loanAmount(new BigDecimal("10000.00"))
                .loanPurpose("Debt Consolidation")
                .requestedTermMonths(24)
                .build();

        LoanApplication application3 = LoanApplication.builder()
                .customer(customer2)
                .loanAmount(new BigDecimal("15000.00"))
                .loanPurpose("Car Purchase")
                .requestedTermMonths(48)
                .build();

        loanApplicationRepository.saveAll(List.of(application1, application2, application3));

        // Test finding by loan amount between 12000 and 20000
        List<LoanApplication> mediumLoans = loanApplicationRepository.findByLoanAmountBetween(
                new BigDecimal("12000.00"), new BigDecimal("20000.00"));
        assertThat(mediumLoans).hasSize(1);
        assertThat(mediumLoans.get(0).getLoanPurpose()).isEqualTo("Car Purchase");
    }

    @Test
    public void testFindByRiskLevel() {
        // Create and save loan applications with risk levels
        LoanApplication application1 = LoanApplication.builder()
                .customer(customer1)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("Home Renovation")
                .requestedTermMonths(36)
                .riskLevel("LOW")
                .riskScore(25)
                .build();

        LoanApplication application2 = LoanApplication.builder()
                .customer(customer1)
                .loanAmount(new BigDecimal("10000.00"))
                .loanPurpose("Debt Consolidation")
                .requestedTermMonths(24)
                .riskLevel("MEDIUM")
                .riskScore(50)
                .build();

        LoanApplication application3 = LoanApplication.builder()
                .customer(customer2)
                .loanAmount(new BigDecimal("15000.00"))
                .loanPurpose("Car Purchase")
                .requestedTermMonths(48)
                .riskLevel("HIGH")
                .riskScore(75)
                .build();

        loanApplicationRepository.saveAll(List.of(application1, application2, application3));

        // Test finding by risk level
        List<LoanApplication> mediumRiskApplications = loanApplicationRepository.findByRiskLevel("MEDIUM");
        assertThat(mediumRiskApplications).hasSize(1);
        assertThat(mediumRiskApplications.get(0).getLoanPurpose()).isEqualTo("Debt Consolidation");
    }

    @Test
    public void testFindHighRiskHighValueApplications() {
        // Create and save loan applications with risk scores and amounts
        LoanApplication application1 = LoanApplication.builder()
                .customer(customer1)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("Home Renovation")
                .requestedTermMonths(36)
                .riskScore(60)
                .build();

        LoanApplication application2 = LoanApplication.builder()
                .customer(customer1)
                .loanAmount(new BigDecimal("10000.00"))
                .loanPurpose("Debt Consolidation")
                .requestedTermMonths(24)
                .riskScore(40)
                .build();

        LoanApplication application3 = LoanApplication.builder()
                .customer(customer2)
                .loanAmount(new BigDecimal("30000.00"))
                .loanPurpose("Business Loan")
                .requestedTermMonths(60)
                .riskScore(70)
                .build();

        loanApplicationRepository.saveAll(List.of(application1, application2, application3));

        // Test finding high risk, high value applications (risk score >= 60 and amount >= 20000)
        List<LoanApplication> highRiskHighValueApplications = loanApplicationRepository
                .findHighRiskHighValueApplications(60, new BigDecimal("20000.00"));
        
        assertThat(highRiskHighValueApplications).hasSize(2);
        assertThat(highRiskHighValueApplications).extracting(LoanApplication::getLoanPurpose)
                .containsExactlyInAnyOrder("Home Renovation", "Business Loan");
    }
}