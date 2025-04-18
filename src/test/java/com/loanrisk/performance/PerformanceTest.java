package com.loanrisk.performance;

import com.loanrisk.engine.RuleEngine;
import com.loanrisk.engine.calculator.DerivedFieldCalculator;
import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;
import com.loanrisk.model.entity.ScoringRule;
import com.loanrisk.repository.CustomerRepository;
import com.loanrisk.repository.LoanApplicationRepository;
import com.loanrisk.repository.ScoringRuleRepository;
import com.loanrisk.service.LoanEvaluationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Performance tests for the Loan Risk Engine
 * Tests the performance of the rule engine and loan evaluation service
 * with a large number of rules and loan applications
 */
@SpringBootTest
@ActiveProfiles("test")
public class PerformanceTest {

    @Autowired
    private RuleEngine ruleEngine;

    @Autowired
    private LoanEvaluationService loanEvaluationService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private ScoringRuleRepository scoringRuleRepository;

    @Autowired
    private DerivedFieldCalculator derivedFieldCalculator;

    private Random random = new Random();
    private List<Customer> testCustomers = new ArrayList<>();
    private List<LoanApplication> testLoanApplications = new ArrayList<>();

    @BeforeEach
    public void setup() {
        // Clear existing test data
        testCustomers.clear();
        testLoanApplications.clear();
        
        // Create test customers first
        createTestCustomers(100);
        
        // Then create test loan applications
        createTestLoanApplications(100);
    }

    /**
     * Test the performance of the rule engine with a large number of rules
     */
    @Test
    @Transactional
    public void testRuleEnginePerformanceWithManyRules() {
        // Create a large number of test rules
        int numberOfRules = 50;
        List<ScoringRule> testRules = createTestRules(numberOfRules);
        
        // Save the rules to the database
        scoringRuleRepository.saveAll(testRules);
        
        // Select a random customer and loan application for testing
        Customer customer = testCustomers.get(random.nextInt(testCustomers.size()));
        LoanApplication loanApplication = testLoanApplications.get(random.nextInt(testLoanApplications.size()));
        loanApplication.setCustomer(customer);
        
        // Calculate derived fields
        Map<String, Object> derivedFields = derivedFieldCalculator.calculateDerivedFields(customer, loanApplication);
        
        // Measure the time it takes to evaluate all rules
        Instant start = Instant.now();
        
        List<ScoringRule> triggeredRules = ruleEngine.evaluateRules(loanApplication, customer, derivedFields);
        
        Instant end = Instant.now();
        long durationMillis = Duration.between(start, end).toMillis();
        
        // Log the performance results
        System.out.println("Rule Engine Performance Test Results:");
        System.out.println("Number of rules: " + numberOfRules);
        System.out.println("Time to evaluate rules: " + durationMillis + " ms");
        System.out.println("Number of triggered rules: " + triggeredRules.size());
        
        // Assert that the evaluation completes within a reasonable time
        // Adjust the threshold based on your performance requirements
        assertTrue(durationMillis < 1000, "Rule evaluation took too long: " + durationMillis + " ms");
        
        // Clean up the test rules
        scoringRuleRepository.deleteAll(testRules);
    }

    /**
     * Test the performance of the loan evaluation service with a large number of loan applications
     */
    @Test
    @Transactional
    public void testLoanEvaluationPerformanceWithManyApplications() {
        // Number of loan applications to evaluate
        int numberOfApplications = 50;
        
        // Select random customers and loan applications for testing
        List<LoanApplication> applicationsToEvaluate = new ArrayList<>();
        for (int i = 0; i < numberOfApplications; i++) {
            Customer customer = testCustomers.get(random.nextInt(testCustomers.size()));
            LoanApplication loanApplication = testLoanApplications.get(random.nextInt(testLoanApplications.size()));
            loanApplication.setCustomer(customer);
            applicationsToEvaluate.add(loanApplication);
        }
        
        // Measure the time it takes to evaluate all loan applications
        Instant start = Instant.now();
        
        for (LoanApplication application : applicationsToEvaluate) {
            loanEvaluationService.evaluateLoanApplication(application);
        }
        
        Instant end = Instant.now();
        long durationMillis = Duration.between(start, end).toMillis();
        
        // Calculate average time per application
        double averageTimePerApplication = (double) durationMillis / numberOfApplications;
        
        // Log the performance results
        System.out.println("Loan Evaluation Performance Test Results:");
        System.out.println("Number of applications: " + numberOfApplications);
        System.out.println("Total evaluation time: " + durationMillis + " ms");
        System.out.println("Average time per application: " + averageTimePerApplication + " ms");
        
        // Assert that the evaluation completes within a reasonable time
        // Adjust the threshold based on your performance requirements
        assertTrue(averageTimePerApplication < 100, 
                "Average loan evaluation time too high: " + averageTimePerApplication + " ms");
    }

    /**
     * Test the performance of the rule engine under load
     */
    @Test
    @Transactional
    public void testRuleEnginePerformanceUnderLoad() {
        // Create a moderate number of test rules
        int numberOfRules = 20;
        List<ScoringRule> testRules = createTestRules(numberOfRules);
        
        // Save the rules to the database
        scoringRuleRepository.saveAll(testRules);
        
        // Number of evaluations to perform
        int numberOfEvaluations = 100;
        
        // Measure the time it takes to perform multiple evaluations
        Instant start = Instant.now();
        
        for (int i = 0; i < numberOfEvaluations; i++) {
            // Select a random customer and loan application for each evaluation
            Customer customer = testCustomers.get(random.nextInt(testCustomers.size()));
            LoanApplication loanApplication = testLoanApplications.get(random.nextInt(testLoanApplications.size()));
            loanApplication.setCustomer(customer);
            
            // Calculate derived fields
            Map<String, Object> derivedFields = derivedFieldCalculator.calculateDerivedFields(customer, loanApplication);
            
            // Evaluate rules
            ruleEngine.evaluateRules(loanApplication, customer, derivedFields);
        }
        
        Instant end = Instant.now();
        long durationMillis = Duration.between(start, end).toMillis();
        
        // Calculate average time per evaluation
        double averageTimePerEvaluation = (double) durationMillis / numberOfEvaluations;
        
        // Log the performance results
        System.out.println("Rule Engine Load Test Results:");
        System.out.println("Number of rules: " + numberOfRules);
        System.out.println("Number of evaluations: " + numberOfEvaluations);
        System.out.println("Total evaluation time: " + durationMillis + " ms");
        System.out.println("Average time per evaluation: " + averageTimePerEvaluation + " ms");
        
        // Assert that the evaluation completes within a reasonable time
        // Adjust the threshold based on your performance requirements
        assertTrue(averageTimePerEvaluation < 50, 
                "Average rule evaluation time too high: " + averageTimePerEvaluation + " ms");
        
        // Clean up the test rules
        scoringRuleRepository.deleteAll(testRules);
    }

    /**
     * Create test customers for performance testing
     */
    private void createTestCustomers(int count) {
        for (int i = 0; i < count; i++) {
            Customer customer = Customer.builder()
                    .name("Test Customer " + i)
                    .age(random.nextInt(50) + 18) // Random age between 18 and 67
                    .annualIncome(new BigDecimal(random.nextInt(100000) + 20000)) // Random income between 20k and 120k
                    .creditScore(random.nextInt(500) + 300) // Random credit score between 300 and 799
                    .employmentStatus(random.nextBoolean() ? "EMPLOYED" : "SELF_EMPLOYED") // Random employment status
                    .existingDebt(new BigDecimal(random.nextInt(50000))) // Random debt between 0 and 50k
                    .build();
            
            testCustomers.add(customer);
        }
        
        // Save the customers to the database
        customerRepository.saveAll(testCustomers);
    }

    /**
     * Create test loan applications for performance testing
     */
    private void createTestLoanApplications(int count) {
        // Make sure we have customers before creating loan applications
        if (testCustomers.isEmpty() || testCustomers.size() < count) {
            createTestCustomers(Math.max(count, 100));
        }
        
        for (int i = 0; i < count; i++) {
            // Get a random customer for this loan application
            Customer customer = testCustomers.get(random.nextInt(testCustomers.size()));
            
            LoanApplication loanApplication = LoanApplication.builder()
                    .customer(customer) // Set the customer for the loan application
                    .loanAmount(new BigDecimal(random.nextInt(90000) + 10000)) // Random amount between 10k and 100k
                    .loanPurpose("Test Purpose " + i)
                    .requestedTermMonths(random.nextInt(5) * 12 + 12) // Random term: 12, 24, 36, 48, or 60 months
                    .build();
            
            testLoanApplications.add(loanApplication);
        }
        
        // Save the loan applications to the database
        loanApplicationRepository.saveAll(testLoanApplications);
    }

    /**
     * Create test scoring rules for performance testing
     */
    private List<ScoringRule> createTestRules(int count) {
        List<ScoringRule> rules = new ArrayList<>();
        
        String[] fields = {"creditScore", "age", "annualIncome", "existingDebt", "employmentStatus", 
                "loanAmount", "requestedTermMonths", "debtToIncomeRatio", "loanToIncomeRatio"};
        
        String[] operators = {"EQUALS", "NOT_EQUALS", "GREATER_THAN", "LESS_THAN", 
                "GREATER_THAN_OR_EQUAL", "LESS_THAN_OR_EQUAL", "CONTAINS", "NOT_CONTAINS", 
                "STARTS_WITH", "ENDS_WITH"};
        
        for (int i = 0; i < count; i++) {
            String field = fields[random.nextInt(fields.length)];
            String operator = operators[random.nextInt(operators.length)];
            String ruleValue;
            
            // Generate appropriate rule values based on the field
            switch (field) {
                case "creditScore":
                    ruleValue = String.valueOf(random.nextInt(500) + 300); // Between 300 and 799
                    break;
                case "age":
                    ruleValue = String.valueOf(random.nextInt(50) + 18); // Between 18 and 67
                    break;
                case "annualIncome":
                    ruleValue = String.valueOf(random.nextInt(100000) + 20000); // Between 20k and 120k
                    break;
                case "existingDebt":
                    ruleValue = String.valueOf(random.nextInt(50000)); // Between 0 and 50k
                    break;
                case "employmentStatus":
                    ruleValue = random.nextBoolean() ? "EMPLOYED" : "SELF_EMPLOYED";
                    break;
                case "loanAmount":
                    ruleValue = String.valueOf(random.nextInt(90000) + 10000); // Between 10k and 100k
                    break;
                case "requestedTermMonths":
                    ruleValue = String.valueOf(random.nextInt(5) * 12 + 12); // 12, 24, 36, 48, or 60
                    break;
                case "debtToIncomeRatio":
                    ruleValue = String.format("%.2f", random.nextDouble() * 0.8); // Between 0 and 0.8
                    break;
                case "loanToIncomeRatio":
                    ruleValue = String.format("%.2f", random.nextDouble() * 0.8); // Between 0 and 0.8
                    break;
                default:
                    ruleValue = "test";
            }
            
            ScoringRule rule = ScoringRule.builder()
                    .name("Test Rule " + i)
                    .field(field)
                    .operator(operator)
                    .ruleValue(ruleValue)
                    .riskPoints(random.nextInt(40) + 5) // Random points between 5 and 44
                    .priority(i + 1) // Priority based on order
                    .enabled(true)
                    .build();
            
            rules.add(rule);
        }
        
        return rules;
    }
}