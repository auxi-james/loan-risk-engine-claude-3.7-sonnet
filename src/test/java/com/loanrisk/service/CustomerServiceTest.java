package com.loanrisk.service;

import com.loanrisk.exception.ResourceNotFoundException;
import com.loanrisk.model.dto.CustomerRequestDto;
import com.loanrisk.model.dto.CustomerResponseDto;
import com.loanrisk.model.entity.Customer;
import com.loanrisk.repository.CustomerRepository;
import com.loanrisk.service.impl.CustomerServiceImpl;
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
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerRequestDto customerRequestDto;
    private final Long customerId = 1L;
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

        // Setup test customer request DTO
        customerRequestDto = CustomerRequestDto.builder()
                .name("John Doe")
                .age(35)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("25000.00"))
                .build();
    }

    @Test
    void createCustomer_ShouldReturnCustomerResponseDto() {
        // Arrange
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // Act
        CustomerResponseDto result = customerService.createCustomer(customerRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals(customerRequestDto.getName(), result.getName());
        assertEquals(customerRequestDto.getAge(), result.getAge());
        assertEquals(customerRequestDto.getAnnualIncome(), result.getAnnualIncome());
        assertEquals(customerRequestDto.getCreditScore(), result.getCreditScore());
        assertEquals(customerRequestDto.getEmploymentStatus(), result.getEmploymentStatus());
        assertEquals(customerRequestDto.getExistingDebt(), result.getExistingDebt());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void getCustomerById_WithValidId_ShouldReturnCustomerResponseDto() {
        // Arrange
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        // Act
        CustomerResponseDto result = customerService.getCustomerById(customerId);

        // Assert
        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals(customer.getName(), result.getName());
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void getCustomerById_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(customerId));
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void getAllCustomers_ShouldReturnListOfCustomerResponseDto() {
        // Arrange
        Customer customer2 = Customer.builder()
                .id(2L)
                .name("Jane Smith")
                .age(28)
                .annualIncome(new BigDecimal("65000.00"))
                .creditScore(680)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer, customer2));

        // Act
        List<CustomerResponseDto> result = customerService.getAllCustomers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(customerId, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void updateCustomer_WithValidId_ShouldReturnUpdatedCustomerResponseDto() {
        // Arrange
        CustomerRequestDto updateRequest = CustomerRequestDto.builder()
                .name("John Doe Updated")
                .age(36)
                .annualIncome(new BigDecimal("80000.00"))
                .creditScore(730)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("20000.00"))
                .build();
        
        Customer updatedCustomer = Customer.builder()
                .id(customerId)
                .name(updateRequest.getName())
                .age(updateRequest.getAge())
                .annualIncome(updateRequest.getAnnualIncome())
                .creditScore(updateRequest.getCreditScore())
                .employmentStatus(updateRequest.getEmploymentStatus())
                .existingDebt(updateRequest.getExistingDebt())
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        // Act
        CustomerResponseDto result = customerService.updateCustomer(customerId, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(customerId, result.getId());
        assertEquals(updateRequest.getName(), result.getName());
        assertEquals(updateRequest.getAge(), result.getAge());
        assertEquals(updateRequest.getAnnualIncome(), result.getAnnualIncome());
        assertEquals(updateRequest.getCreditScore(), result.getCreditScore());
        assertEquals(updateRequest.getEmploymentStatus(), result.getEmploymentStatus());
        assertEquals(updateRequest.getExistingDebt(), result.getExistingDebt());
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void updateCustomer_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            customerService.updateCustomer(customerId, customerRequestDto));
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deleteCustomer_WithValidId_ShouldDeleteCustomer() {
        // Arrange
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(customer);

        // Act
        customerService.deleteCustomer(customerId);

        // Assert
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void deleteCustomer_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomer(customerId));
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, never()).delete(any(Customer.class));
    }

    @Test
    void findCustomersByName_ShouldReturnMatchingCustomers() {
        // Arrange
        String nameQuery = "John";
        when(customerRepository.findByNameContainingIgnoreCase(nameQuery))
            .thenReturn(Arrays.asList(customer));

        // Act
        List<CustomerResponseDto> result = customerService.findCustomersByName(nameQuery);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customer.getName(), result.get(0).getName());
        verify(customerRepository, times(1)).findByNameContainingIgnoreCase(nameQuery);
    }

    @Test
    void findCustomersByCreditScoreRange_ShouldReturnMatchingCustomers() {
        // Arrange
        Integer minScore = 700;
        Integer maxScore = 750;
        when(customerRepository.findByCreditScoreBetween(minScore, maxScore))
            .thenReturn(Arrays.asList(customer));

        // Act
        List<CustomerResponseDto> result = customerService.findCustomersByCreditScoreRange(minScore, maxScore);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customer.getCreditScore(), result.get(0).getCreditScore());
        verify(customerRepository, times(1)).findByCreditScoreBetween(minScore, maxScore);
    }

    @Test
    void findCustomersByEmploymentStatus_ShouldReturnMatchingCustomers() {
        // Arrange
        String status = "EMPLOYED";
        when(customerRepository.findByEmploymentStatus(status))
            .thenReturn(Arrays.asList(customer));

        // Act
        List<CustomerResponseDto> result = customerService.findCustomersByEmploymentStatus(status);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customer.getEmploymentStatus(), result.get(0).getEmploymentStatus());
        verify(customerRepository, times(1)).findByEmploymentStatus(status);
    }

    @Test
    void findCustomersByMinimumIncome_ShouldReturnMatchingCustomers() {
        // Arrange
        BigDecimal minIncome = new BigDecimal("70000.00");
        when(customerRepository.findByAnnualIncomeGreaterThanEqual(minIncome))
            .thenReturn(Arrays.asList(customer));

        // Act
        List<CustomerResponseDto> result = customerService.findCustomersByMinimumIncome(minIncome);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getAnnualIncome().compareTo(minIncome) >= 0);
        verify(customerRepository, times(1)).findByAnnualIncomeGreaterThanEqual(minIncome);
    }

    @Test
    void findCustomersByMaximumDebt_ShouldReturnMatchingCustomers() {
        // Arrange
        BigDecimal maxDebt = new BigDecimal("30000.00");
        when(customerRepository.findByExistingDebtLessThan(maxDebt))
            .thenReturn(Arrays.asList(customer));

        // Act
        List<CustomerResponseDto> result = customerService.findCustomersByMaximumDebt(maxDebt);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getExistingDebt().compareTo(maxDebt) < 0);
        verify(customerRepository, times(1)).findByExistingDebtLessThan(maxDebt);
    }

    @Test
    void findCustomersByAgeRange_ShouldReturnMatchingCustomers() {
        // Arrange
        Integer minAge = 30;
        Integer maxAge = 40;
        when(customerRepository.findByAgeBetween(minAge, maxAge))
            .thenReturn(Arrays.asList(customer));

        // Act
        List<CustomerResponseDto> result = customerService.findCustomersByAgeRange(minAge, maxAge);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getAge() >= minAge && result.get(0).getAge() <= maxAge);
        verify(customerRepository, times(1)).findByAgeBetween(minAge, maxAge);
    }
}