package com.loanrisk.service;

import com.loanrisk.exception.ResourceNotFoundException;
import com.loanrisk.model.dto.LoanApplicationRequestDto;
import com.loanrisk.model.dto.LoanApplicationResponseDto;
import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;
import com.loanrisk.repository.CustomerRepository;
import com.loanrisk.repository.LoanApplicationRepository;
import com.loanrisk.service.impl.LoanApplicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanApplicationServiceTest {

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private LoanApplicationServiceImpl loanApplicationService;

    private Customer customer;
    private LoanApplication loanApplication;
    private LoanApplicationRequestDto loanApplicationRequestDto;
    private final Long customerId = 1L;
    private final Long loanApplicationId = 1L;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Setup test customer
        customer = Customer.builder()
                .id(customerId)
                .name("John Doe")
                .age(35)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("25000.00"))
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Setup test loan application
        loanApplication = LoanApplication.builder()
                .id(loanApplicationId)
                .customer(customer)
                .loanAmount(new BigDecimal("150000.00"))
                .loanPurpose("HOME_PURCHASE")
                .requestedTermMonths(360)
                .riskScore(25)
                .riskLevel("LOW")
                .decision("APPROVED")
                .explanation("Low risk application approved automatically.")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Setup test loan application request DTO
        loanApplicationRequestDto = LoanApplicationRequestDto.builder()
                .customerId(customerId)
                .loanAmount(new BigDecimal("150000.00"))
                .loanPurpose("HOME_PURCHASE")
                .requestedTermMonths(360)
                .build();
    }

    @Test
    void createLoanApplication_ShouldReturnLoanApplicationResponseDto() {
        // Arrange
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(loanApplication);

        // Act
        LoanApplicationResponseDto result = loanApplicationService.createLoanApplication(loanApplicationRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(loanApplicationId, result.getId());
        assertEquals(customerId, result.getCustomerId());
        assertEquals(loanApplicationRequestDto.getLoanAmount(), result.getLoanAmount());
        assertEquals(loanApplicationRequestDto.getLoanPurpose(), result.getLoanPurpose());
        assertEquals(loanApplicationRequestDto.getRequestedTermMonths(), result.getRequestedTermMonths());
        verify(customerRepository, times(1)).findById(customerId);
        verify(loanApplicationRepository, times(1)).save(any(LoanApplication.class));
    }

    @Test
    void createLoanApplication_WithInvalidCustomerId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            loanApplicationService.createLoanApplication(loanApplicationRequestDto));
        verify(customerRepository, times(1)).findById(customerId);
        verify(loanApplicationRepository, never()).save(any(LoanApplication.class));
    }

    @Test
    void getLoanApplicationById_WithValidId_ShouldReturnLoanApplicationResponseDto() {
        // Arrange
        when(loanApplicationRepository.findById(loanApplicationId)).thenReturn(Optional.of(loanApplication));

        // Act
        LoanApplicationResponseDto result = loanApplicationService.getLoanApplicationById(loanApplicationId);

        // Assert
        assertNotNull(result);
        assertEquals(loanApplicationId, result.getId());
        assertEquals(customerId, result.getCustomerId());
        assertEquals(loanApplication.getLoanAmount(), result.getLoanAmount());
        assertEquals(loanApplication.getLoanPurpose(), result.getLoanPurpose());
        verify(loanApplicationRepository, times(1)).findById(loanApplicationId);
    }

    @Test
    void getLoanApplicationById_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(loanApplicationRepository.findById(loanApplicationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            loanApplicationService.getLoanApplicationById(loanApplicationId));
        verify(loanApplicationRepository, times(1)).findById(loanApplicationId);
    }

    @Test
    void getAllLoanApplications_ShouldReturnListOfLoanApplicationResponseDto() {
        // Arrange
        LoanApplication loanApplication2 = LoanApplication.builder()
                .id(2L)
                .customer(customer)
                .loanAmount(new BigDecimal("50000.00"))
                .loanPurpose("AUTO_LOAN")
                .requestedTermMonths(60)
                .riskScore(35)
                .riskLevel("MEDIUM")
                .decision("MANUAL_REVIEW")
                .explanation("Medium risk application requires manual review.")
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        when(loanApplicationRepository.findAll()).thenReturn(Arrays.asList(loanApplication, loanApplication2));

        // Act
        List<LoanApplicationResponseDto> result = loanApplicationService.getAllLoanApplications();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(loanApplicationId, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(loanApplicationRepository, times(1)).findAll();
    }

    @Test
    void updateLoanApplication_WithValidId_ShouldReturnUpdatedLoanApplicationResponseDto() {
        // Arrange
        LoanApplicationRequestDto updateRequest = LoanApplicationRequestDto.builder()
                .customerId(customerId)
                .loanAmount(new BigDecimal("200000.00"))
                .loanPurpose("HOME_RENOVATION")
                .requestedTermMonths(240)
                .build();
        
        LoanApplication updatedLoanApplication = LoanApplication.builder()
                .id(loanApplicationId)
                .customer(customer)
                .loanAmount(updateRequest.getLoanAmount())
                .loanPurpose(updateRequest.getLoanPurpose())
                .requestedTermMonths(updateRequest.getRequestedTermMonths())
                .riskScore(25)
                .riskLevel("LOW")
                .decision("APPROVED")
                .explanation("Low risk application approved automatically.")
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        when(loanApplicationRepository.findById(loanApplicationId)).thenReturn(Optional.of(loanApplication));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(updatedLoanApplication);

        // Act
        LoanApplicationResponseDto result = loanApplicationService.updateLoanApplication(loanApplicationId, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(loanApplicationId, result.getId());
        assertEquals(customerId, result.getCustomerId());
        assertEquals(updateRequest.getLoanAmount(), result.getLoanAmount());
        assertEquals(updateRequest.getLoanPurpose(), result.getLoanPurpose());
        assertEquals(updateRequest.getRequestedTermMonths(), result.getRequestedTermMonths());
        verify(loanApplicationRepository, times(1)).findById(loanApplicationId);
        verify(customerRepository, times(1)).findById(customerId);
        verify(loanApplicationRepository, times(1)).save(any(LoanApplication.class));
    }

    @Test
    void updateLoanApplication_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(loanApplicationRepository.findById(loanApplicationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            loanApplicationService.updateLoanApplication(loanApplicationId, loanApplicationRequestDto));
        verify(loanApplicationRepository, times(1)).findById(loanApplicationId);
        verify(customerRepository, never()).findById(any(Long.class));
        verify(loanApplicationRepository, never()).save(any(LoanApplication.class));
    }

    @Test
    void updateLoanApplication_WithInvalidCustomerId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(loanApplicationRepository.findById(loanApplicationId)).thenReturn(Optional.of(loanApplication));
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            loanApplicationService.updateLoanApplication(loanApplicationId, loanApplicationRequestDto));
        verify(loanApplicationRepository, times(1)).findById(loanApplicationId);
        verify(customerRepository, times(1)).findById(customerId);
        verify(loanApplicationRepository, never()).save(any(LoanApplication.class));
    }

    @Test
    void deleteLoanApplication_WithValidId_ShouldDeleteLoanApplication() {
        // Arrange
        when(loanApplicationRepository.findById(loanApplicationId)).thenReturn(Optional.of(loanApplication));
        doNothing().when(loanApplicationRepository).delete(loanApplication);

        // Act
        loanApplicationService.deleteLoanApplication(loanApplicationId);

        // Assert
        verify(loanApplicationRepository, times(1)).findById(loanApplicationId);
        verify(loanApplicationRepository, times(1)).delete(loanApplication);
    }

    @Test
    void deleteLoanApplication_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(loanApplicationRepository.findById(loanApplicationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            loanApplicationService.deleteLoanApplication(loanApplicationId));
        verify(loanApplicationRepository, times(1)).findById(loanApplicationId);
        verify(loanApplicationRepository, never()).delete(any(LoanApplication.class));
    }

    @Test
    void findLoanApplicationsByCustomer_ShouldReturnMatchingLoanApplications() {
        // Arrange
        when(loanApplicationRepository.findByCustomer(customer)).thenReturn(Arrays.asList(loanApplication));

        // Act
        List<LoanApplicationResponseDto> result = loanApplicationService.findLoanApplicationsByCustomer(customer);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(loanApplicationId, result.get(0).getId());
        assertEquals(customerId, result.get(0).getCustomerId());
        verify(loanApplicationRepository, times(1)).findByCustomer(customer);
    }

    @Test
    void findLoanApplicationsByCustomerId_ShouldReturnMatchingLoanApplications() {
        // Arrange
        when(loanApplicationRepository.findByCustomerId(customerId)).thenReturn(Arrays.asList(loanApplication));

        // Act
        List<LoanApplicationResponseDto> result = loanApplicationService.findLoanApplicationsByCustomerId(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(loanApplicationId, result.get(0).getId());
        assertEquals(customerId, result.get(0).getCustomerId());
        verify(loanApplicationRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    void evaluateLoanApplication_ShouldReturnEvaluatedLoanApplication() {
        // Arrange
        when(loanApplicationRepository.findById(loanApplicationId)).thenReturn(Optional.of(loanApplication));
        when(loanApplicationRepository.save(any(LoanApplication.class))).thenReturn(loanApplication);

        // Act
        LoanApplicationResponseDto result = loanApplicationService.evaluateLoanApplication(loanApplicationId);

        // Assert
        assertNotNull(result);
        assertEquals(loanApplicationId, result.getId());
        assertNotNull(result.getRiskScore());
        assertNotNull(result.getRiskLevel());
        assertNotNull(result.getDecision());
        assertNotNull(result.getExplanation());
        verify(loanApplicationRepository, times(1)).findById(loanApplicationId);
        verify(loanApplicationRepository, times(1)).save(any(LoanApplication.class));
    }
}