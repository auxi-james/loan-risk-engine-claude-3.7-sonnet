package com.loanrisk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanrisk.model.dto.CustomerResponseDto;
import com.loanrisk.model.dto.LoanApplicationRequestDto;
import com.loanrisk.model.dto.LoanApplicationResponseDto;
import com.loanrisk.service.LoanApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanApplicationController.class)
public class LoanApplicationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoanApplicationService loanApplicationService;

    @Test
    public void testSubmitLoanApplication_Success() throws Exception {
        // Arrange
        LoanApplicationRequestDto requestDto = LoanApplicationRequestDto.builder()
                .customerId(1L)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("Home renovation")
                .requestedTermMonths(36)
                .build();

        CustomerResponseDto customerDto = CustomerResponseDto.builder()
                .id(1L)
                .name("John Doe")
                .age(30)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        LoanApplicationResponseDto createdResponseDto = LoanApplicationResponseDto.builder()
                .id(1L)
                .customerId(1L)
                .customer(customerDto)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("Home renovation")
                .requestedTermMonths(36)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        LoanApplicationResponseDto evaluatedResponseDto = LoanApplicationResponseDto.builder()
                .id(1L)
                .customerId(1L)
                .customer(customerDto)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("Home renovation")
                .requestedTermMonths(36)
                .riskScore(25)
                .riskLevel("LOW")
                .decision("APPROVED")
                .explanation("Low risk application approved automatically.")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(loanApplicationService.createLoanApplication(any(LoanApplicationRequestDto.class))).thenReturn(createdResponseDto);
        when(loanApplicationService.evaluateLoanApplication(anyLong())).thenReturn(evaluatedResponseDto);

        // Act & Assert
        mockMvc.perform(post("/loan/apply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.loanAmount").value(25000.00))
                .andExpect(jsonPath("$.loanPurpose").value("Home renovation"))
                .andExpect(jsonPath("$.requestedTermMonths").value(36))
                .andExpect(jsonPath("$.riskScore").value(25))
                .andExpect(jsonPath("$.riskLevel").value("LOW"))
                .andExpect(jsonPath("$.decision").value("APPROVED"));
    }

    @Test
    public void testGetLoanApplicationById_Success() throws Exception {
        // Arrange
        Long loanApplicationId = 1L;
        CustomerResponseDto customerDto = CustomerResponseDto.builder()
                .id(1L)
                .name("John Doe")
                .age(30)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        LoanApplicationResponseDto responseDto = LoanApplicationResponseDto.builder()
                .id(loanApplicationId)
                .customerId(1L)
                .customer(customerDto)
                .loanAmount(new BigDecimal("25000.00"))
                .loanPurpose("Home renovation")
                .requestedTermMonths(36)
                .riskScore(25)
                .riskLevel("LOW")
                .decision("APPROVED")
                .explanation("Low risk application approved automatically.")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(loanApplicationService.getLoanApplicationById(loanApplicationId)).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(get("/loan/{id}", loanApplicationId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.loanAmount").value(25000.00))
                .andExpect(jsonPath("$.loanPurpose").value("Home renovation"))
                .andExpect(jsonPath("$.requestedTermMonths").value(36))
                .andExpect(jsonPath("$.riskScore").value(25))
                .andExpect(jsonPath("$.riskLevel").value("LOW"))
                .andExpect(jsonPath("$.decision").value("APPROVED"));
    }

    @Test
    public void testSubmitLoanApplication_ValidationFailure() throws Exception {
        // Arrange
        LoanApplicationRequestDto requestDto = LoanApplicationRequestDto.builder()
                .customerId(null)  // Invalid: null customer ID
                .loanAmount(new BigDecimal("500.00"))  // Invalid: amount below 1000
                .loanPurpose("")  // Invalid: empty loan purpose
                .requestedTermMonths(3)  // Invalid: term below 6 months
                .build();

        // Act & Assert
        mockMvc.perform(post("/loan/apply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}