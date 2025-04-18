package com.loanrisk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanrisk.model.dto.CustomerRequestDto;
import com.loanrisk.model.dto.CustomerResponseDto;
import com.loanrisk.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    public void testCreateCustomer_Success() throws Exception {
        // Arrange
        CustomerRequestDto requestDto = CustomerRequestDto.builder()
                .name("John Doe")
                .age(30)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .build();

        CustomerResponseDto responseDto = CustomerResponseDto.builder()
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

        when(customerService.createCustomer(any(CustomerRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.annualIncome").value(75000.00))
                .andExpect(jsonPath("$.creditScore").value(720))
                .andExpect(jsonPath("$.employmentStatus").value("EMPLOYED"))
                .andExpect(jsonPath("$.existingDebt").value(15000.00));
    }

    @Test
    public void testGetCustomerById_Success() throws Exception {
        // Arrange
        Long customerId = 1L;
        CustomerResponseDto responseDto = CustomerResponseDto.builder()
                .id(customerId)
                .name("John Doe")
                .age(30)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(customerService.getCustomerById(customerId)).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(get("/customers/{id}", customerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.annualIncome").value(75000.00))
                .andExpect(jsonPath("$.creditScore").value(720))
                .andExpect(jsonPath("$.employmentStatus").value("EMPLOYED"))
                .andExpect(jsonPath("$.existingDebt").value(15000.00));
    }

    @Test
    public void testCreateCustomer_ValidationFailure() throws Exception {
        // Arrange
        CustomerRequestDto requestDto = CustomerRequestDto.builder()
                .name("")  // Invalid: empty name
                .age(15)   // Invalid: age below 18
                .annualIncome(new BigDecimal("-1000.00"))  // Invalid: negative income
                .creditScore(200)  // Invalid: credit score below 300
                .employmentStatus("")  // Invalid: empty employment status
                .existingDebt(new BigDecimal("-100.00"))  // Invalid: negative debt
                .build();

        // Act & Assert
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}