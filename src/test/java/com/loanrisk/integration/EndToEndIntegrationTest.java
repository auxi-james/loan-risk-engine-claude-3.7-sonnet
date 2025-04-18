package com.loanrisk.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanrisk.model.dto.CustomerRequestDto;
import com.loanrisk.model.dto.CustomerResponseDto;
import com.loanrisk.model.dto.LoanApplicationRequestDto;
import com.loanrisk.model.dto.LoanApplicationResponseDto;
import com.loanrisk.model.dto.ScoringRuleResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End-to-end integration tests for the Loan Risk Engine
 * Tests the complete flow from customer creation to loan evaluation
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EndToEndIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test the complete customer creation and retrieval flow
     */
    @Test
    public void testCustomerCreationAndRetrievalFlow() throws Exception {
        // Create a customer
        CustomerRequestDto customerRequest = CustomerRequestDto.builder()
                .name("John Smith")
                .age(35)
                .annualIncome(new BigDecimal("85000.00"))
                .creditScore(750)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("20000.00"))
                .build();

        // Submit the customer creation request
        MvcResult createResult = mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(35))
                .andExpect(jsonPath("$.annualIncome").value(85000.00))
                .andExpect(jsonPath("$.creditScore").value(750))
                .andExpect(jsonPath("$.employmentStatus").value("EMPLOYED"))
                .andExpect(jsonPath("$.existingDebt").value(20000.00))
                .andReturn();

        // Extract the customer ID from the response
        String createResponseJson = createResult.getResponse().getContentAsString();
        CustomerResponseDto createdCustomer = objectMapper.readValue(createResponseJson, CustomerResponseDto.class);
        Long customerId = createdCustomer.getId();

        // Retrieve the customer by ID
        mockMvc.perform(get("/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customerId))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(35))
                .andExpect(jsonPath("$.annualIncome").value(85000.00))
                .andExpect(jsonPath("$.creditScore").value(750))
                .andExpect(jsonPath("$.employmentStatus").value("EMPLOYED"))
                .andExpect(jsonPath("$.existingDebt").value(20000.00));
    }

    /**
     * Test the complete loan application submission and evaluation flow
     */
    @Test
    public void testLoanApplicationSubmissionAndEvaluationFlow() throws Exception {
        // First create a customer
        CustomerRequestDto customerRequest = CustomerRequestDto.builder()
                .name("Jane Doe")
                .age(28)
                .annualIncome(new BigDecimal("65000.00"))
                .creditScore(680)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .build();

        // Submit the customer creation request
        MvcResult createCustomerResult = mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // Extract the customer ID from the response
        String createCustomerResponseJson = createCustomerResult.getResponse().getContentAsString();
        CustomerResponseDto createdCustomer = objectMapper.readValue(createCustomerResponseJson, CustomerResponseDto.class);
        Long customerId = createdCustomer.getId();

        // Create a loan application
        LoanApplicationRequestDto loanRequest = LoanApplicationRequestDto.builder()
                .customerId(customerId)
                .loanAmount(new BigDecimal("30000.00"))
                .loanPurpose("Home renovation")
                .requestedTermMonths(36)
                .build();

        // Submit the loan application
        MvcResult createLoanResult = mockMvc.perform(post("/loan/apply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loanRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.loanAmount").value(30000.00))
                .andExpect(jsonPath("$.loanPurpose").value("Home renovation"))
                .andExpect(jsonPath("$.requestedTermMonths").value(36))
                .andExpect(jsonPath("$.riskScore").exists())
                .andExpect(jsonPath("$.riskLevel").exists())
                .andExpect(jsonPath("$.decision").exists())
                .andReturn();

        // Extract the loan application ID from the response
        String createLoanResponseJson = createLoanResult.getResponse().getContentAsString();
        LoanApplicationResponseDto createdLoan = objectMapper.readValue(createLoanResponseJson, LoanApplicationResponseDto.class);
        Long loanId = createdLoan.getId();

        // Retrieve the loan application by ID
        MvcResult getLoanResult = mockMvc.perform(get("/loan/{id}", loanId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loanId))
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.loanAmount").value(30000.00))
                .andExpect(jsonPath("$.loanPurpose").value("Home renovation"))
                .andExpect(jsonPath("$.requestedTermMonths").value(36))
                .andReturn();

        // Verify the risk evaluation results
        String getLoanResponseJson = getLoanResult.getResponse().getContentAsString();
        LoanApplicationResponseDto retrievedLoan = objectMapper.readValue(getLoanResponseJson, LoanApplicationResponseDto.class);
        
        assertNotNull(retrievedLoan.getRiskScore());
        assertNotNull(retrievedLoan.getRiskLevel());
        assertNotNull(retrievedLoan.getDecision());
        assertNotNull(retrievedLoan.getExplanation());
        
        // Verify that the risk level is one of the expected values
        assertTrue(List.of("LOW", "MEDIUM", "HIGH").contains(retrievedLoan.getRiskLevel()));
        
        // Verify that the decision is one of the expected values
        assertTrue(List.of("APPROVED", "MANUAL_REVIEW", "REJECTED").contains(retrievedLoan.getDecision()));
    }

    /**
     * Test the rule retrieval flow
     */
    @Test
    public void testRuleRetrievalFlow() throws Exception {
        // Retrieve all active scoring rules
        MvcResult getRulesResult = mockMvc.perform(get("/rules")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn();

        // Extract the rules from the response
        String getRulesResponseJson = getRulesResult.getResponse().getContentAsString();
        ScoringRuleResponseDto[] rules = objectMapper.readValue(getRulesResponseJson, ScoringRuleResponseDto[].class);

        // Verify that rules were returned
        assertTrue(rules.length > 0, "No scoring rules were returned");

        // Verify that each rule has the required fields
        for (ScoringRuleResponseDto rule : rules) {
            assertNotNull(rule.getId());
            assertNotNull(rule.getName());
            assertNotNull(rule.getField());
            assertNotNull(rule.getOperator());
            assertNotNull(rule.getRuleValue());
            assertNotNull(rule.getRiskPoints());
            assertNotNull(rule.getPriority());
            assertTrue(rule.getEnabled());
        }
    }

    /**
     * Test different customer profiles with loan applications
     */
    @Test
    public void testDifferentCustomerProfilesWithLoanApplications() throws Exception {
        // Test with a high-risk customer profile
        CustomerRequestDto highRiskCustomer = CustomerRequestDto.builder()
                .name("High Risk Customer")
                .age(22)
                .annualIncome(new BigDecimal("30000.00"))
                .creditScore(550)
                .employmentStatus("UNEMPLOYED")
                .existingDebt(new BigDecimal("25000.00"))
                .build();

        // Create the high-risk customer
        MvcResult createHighRiskCustomerResult = mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(highRiskCustomer)))
                .andExpect(status().isCreated())
                .andReturn();

        String highRiskCustomerJson = createHighRiskCustomerResult.getResponse().getContentAsString();
        CustomerResponseDto createdHighRiskCustomer = objectMapper.readValue(highRiskCustomerJson, CustomerResponseDto.class);
        Long highRiskCustomerId = createdHighRiskCustomer.getId();

        // Create a loan application for the high-risk customer
        LoanApplicationRequestDto highRiskLoan = LoanApplicationRequestDto.builder()
                .customerId(highRiskCustomerId)
                .loanAmount(new BigDecimal("20000.00"))
                .loanPurpose("Debt consolidation")
                .requestedTermMonths(24)
                .build();

        // Submit the high-risk loan application
        MvcResult highRiskLoanResult = mockMvc.perform(post("/loan/apply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(highRiskLoan)))
                .andExpect(status().isCreated())
                .andReturn();

        String highRiskLoanJson = highRiskLoanResult.getResponse().getContentAsString();
        LoanApplicationResponseDto highRiskLoanResponse = objectMapper.readValue(highRiskLoanJson, LoanApplicationResponseDto.class);

        // Verify high-risk evaluation
        assertEquals("HIGH", highRiskLoanResponse.getRiskLevel());
        assertEquals("REJECTED", highRiskLoanResponse.getDecision());

        // Test with a low-risk customer profile
        CustomerRequestDto lowRiskCustomer = CustomerRequestDto.builder()
                .name("Low Risk Customer")
                .age(45)
                .annualIncome(new BigDecimal("120000.00"))
                .creditScore(800)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("10000.00"))
                .build();

        // Create the low-risk customer
        MvcResult createLowRiskCustomerResult = mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lowRiskCustomer)))
                .andExpect(status().isCreated())
                .andReturn();

        String lowRiskCustomerJson = createLowRiskCustomerResult.getResponse().getContentAsString();
        CustomerResponseDto createdLowRiskCustomer = objectMapper.readValue(lowRiskCustomerJson, CustomerResponseDto.class);
        Long lowRiskCustomerId = createdLowRiskCustomer.getId();

        // Create a loan application for the low-risk customer
        LoanApplicationRequestDto lowRiskLoan = LoanApplicationRequestDto.builder()
                .customerId(lowRiskCustomerId)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("Home improvement")
                .requestedTermMonths(36)
                .build();

        // Submit the low-risk loan application
        MvcResult lowRiskLoanResult = mockMvc.perform(post("/loan/apply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lowRiskLoan)))
                .andExpect(status().isCreated())
                .andReturn();

        String lowRiskLoanJson = lowRiskLoanResult.getResponse().getContentAsString();
        LoanApplicationResponseDto lowRiskLoanResponse = objectMapper.readValue(lowRiskLoanJson, LoanApplicationResponseDto.class);

        // Verify low-risk evaluation
        // The risk level could be LOW or MEDIUM depending on the exact implementation
        assertTrue(List.of("LOW", "MEDIUM").contains(lowRiskLoanResponse.getRiskLevel()));
        // The decision could be APPROVED or MANUAL_REVIEW depending on the exact implementation
        assertTrue(List.of("APPROVED", "MANUAL_REVIEW").contains(lowRiskLoanResponse.getDecision()));
    }
}