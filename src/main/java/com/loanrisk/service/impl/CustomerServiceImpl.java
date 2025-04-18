package com.loanrisk.service.impl;

import com.loanrisk.exception.ResourceNotFoundException;
import com.loanrisk.model.dto.CustomerRequestDto;
import com.loanrisk.model.dto.CustomerResponseDto;
import com.loanrisk.model.entity.Customer;
import com.loanrisk.repository.CustomerRepository;
import com.loanrisk.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto) {
        Customer customer = mapToEntity(customerRequestDto);
        Customer savedCustomer = customerRepository.save(customer);
        return mapToDto(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        return mapToDto(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerResponseDto updateCustomer(Long id, CustomerRequestDto customerRequestDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        
        // Update customer fields
        customer.setName(customerRequestDto.getName());
        customer.setAge(customerRequestDto.getAge());
        customer.setAnnualIncome(customerRequestDto.getAnnualIncome());
        customer.setCreditScore(customerRequestDto.getCreditScore());
        customer.setEmploymentStatus(customerRequestDto.getEmploymentStatus());
        customer.setExistingDebt(customerRequestDto.getExistingDebt());
        
        Customer updatedCustomer = customerRepository.save(customer);
        return mapToDto(updatedCustomer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
        customerRepository.delete(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> findCustomersByName(String name) {
        List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(name);
        return customers.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> findCustomersByCreditScoreRange(Integer minScore, Integer maxScore) {
        List<Customer> customers = customerRepository.findByCreditScoreBetween(minScore, maxScore);
        return customers.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> findCustomersByEmploymentStatus(String employmentStatus) {
        List<Customer> customers = customerRepository.findByEmploymentStatus(employmentStatus);
        return customers.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> findCustomersByMinimumIncome(BigDecimal minIncome) {
        List<Customer> customers = customerRepository.findByAnnualIncomeGreaterThanEqual(minIncome);
        return customers.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> findCustomersByMaximumDebt(BigDecimal maxDebt) {
        List<Customer> customers = customerRepository.findByExistingDebtLessThan(maxDebt);
        return customers.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> findCustomersByAgeRange(Integer minAge, Integer maxAge) {
        List<Customer> customers = customerRepository.findByAgeBetween(minAge, maxAge);
        return customers.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Map Customer entity to CustomerResponseDto
     */
    private CustomerResponseDto mapToDto(Customer customer) {
        return CustomerResponseDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .age(customer.getAge())
                .annualIncome(customer.getAnnualIncome())
                .creditScore(customer.getCreditScore())
                .employmentStatus(customer.getEmploymentStatus())
                .existingDebt(customer.getExistingDebt())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }

    /**
     * Map CustomerRequestDto to Customer entity
     */
    private Customer mapToEntity(CustomerRequestDto customerRequestDto) {
        return Customer.builder()
                .name(customerRequestDto.getName())
                .age(customerRequestDto.getAge())
                .annualIncome(customerRequestDto.getAnnualIncome())
                .creditScore(customerRequestDto.getCreditScore())
                .employmentStatus(customerRequestDto.getEmploymentStatus())
                .existingDebt(customerRequestDto.getExistingDebt())
                .build();
    }
}