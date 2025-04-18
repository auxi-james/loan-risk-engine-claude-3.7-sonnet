package com.loanrisk.service;

import com.loanrisk.model.dto.CustomerRequestDto;
import com.loanrisk.model.dto.CustomerResponseDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for managing customer operations
 */
public interface CustomerService {
    
    /**
     * Create a new customer
     * 
     * @param customerRequestDto the customer data
     * @return the created customer
     */
    CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto);
    
    /**
     * Get a customer by ID
     * 
     * @param id the customer ID
     * @return the customer
     */
    CustomerResponseDto getCustomerById(Long id);
    
    /**
     * Get all customers
     * 
     * @return list of all customers
     */
    List<CustomerResponseDto> getAllCustomers();
    
    /**
     * Update a customer
     * 
     * @param id the customer ID
     * @param customerRequestDto the updated customer data
     * @return the updated customer
     */
    CustomerResponseDto updateCustomer(Long id, CustomerRequestDto customerRequestDto);
    
    /**
     * Delete a customer
     * 
     * @param id the customer ID
     */
    void deleteCustomer(Long id);
    
    /**
     * Find customers by name (case-insensitive partial match)
     * 
     * @param name the name to search for
     * @return list of matching customers
     */
    List<CustomerResponseDto> findCustomersByName(String name);
    
    /**
     * Find customers by credit score range
     * 
     * @param minScore the minimum credit score
     * @param maxScore the maximum credit score
     * @return list of matching customers
     */
    List<CustomerResponseDto> findCustomersByCreditScoreRange(Integer minScore, Integer maxScore);
    
    /**
     * Find customers by employment status
     * 
     * @param employmentStatus the employment status
     * @return list of matching customers
     */
    List<CustomerResponseDto> findCustomersByEmploymentStatus(String employmentStatus);
    
    /**
     * Find customers with annual income greater than or equal to specified amount
     * 
     * @param minIncome the minimum annual income
     * @return list of matching customers
     */
    List<CustomerResponseDto> findCustomersByMinimumIncome(BigDecimal minIncome);
    
    /**
     * Find customers with existing debt less than specified amount
     * 
     * @param maxDebt the maximum existing debt
     * @return list of matching customers
     */
    List<CustomerResponseDto> findCustomersByMaximumDebt(BigDecimal maxDebt);
    
    /**
     * Find customers by age range
     * 
     * @param minAge the minimum age
     * @param maxAge the maximum age
     * @return list of matching customers
     */
    List<CustomerResponseDto> findCustomersByAgeRange(Integer minAge, Integer maxAge);
}