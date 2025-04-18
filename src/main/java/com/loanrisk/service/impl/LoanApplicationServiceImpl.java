package com.loanrisk.service.impl;

import com.loanrisk.exception.ResourceNotFoundException;
import com.loanrisk.model.dto.CustomerResponseDto;
import com.loanrisk.model.dto.LoanApplicationRequestDto;
import com.loanrisk.model.dto.LoanApplicationResponseDto;
import com.loanrisk.model.entity.Customer;
import com.loanrisk.model.entity.LoanApplication;
import com.loanrisk.repository.CustomerRepository;
import com.loanrisk.repository.LoanApplicationRepository;
import com.loanrisk.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public LoanApplicationServiceImpl(
            LoanApplicationRepository loanApplicationRepository,
            CustomerRepository customerRepository) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public LoanApplicationResponseDto createLoanApplication(LoanApplicationRequestDto loanApplicationRequestDto) {
        Customer customer = customerRepository.findById(loanApplicationRequestDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", loanApplicationRequestDto.getCustomerId()));
        
        LoanApplication loanApplication = mapToEntity(loanApplicationRequestDto, customer);
        LoanApplication savedLoanApplication = loanApplicationRepository.save(loanApplication);
        
        return mapToDto(savedLoanApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanApplicationResponseDto getLoanApplicationById(Long id) {
        LoanApplication loanApplication = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LoanApplication", "id", id));
        
        return mapToDto(loanApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplicationResponseDto> getAllLoanApplications() {
        List<LoanApplication> loanApplications = loanApplicationRepository.findAll();
        
        return loanApplications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LoanApplicationResponseDto updateLoanApplication(Long id, LoanApplicationRequestDto loanApplicationRequestDto) {
        LoanApplication loanApplication = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LoanApplication", "id", id));
        
        Customer customer = customerRepository.findById(loanApplicationRequestDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", loanApplicationRequestDto.getCustomerId()));
        
        // Update loan application fields
        loanApplication.setCustomer(customer);
        loanApplication.setLoanAmount(loanApplicationRequestDto.getLoanAmount());
        loanApplication.setLoanPurpose(loanApplicationRequestDto.getLoanPurpose());
        loanApplication.setRequestedTermMonths(loanApplicationRequestDto.getRequestedTermMonths());
        
        LoanApplication updatedLoanApplication = loanApplicationRepository.save(loanApplication);
        
        return mapToDto(updatedLoanApplication);
    }

    @Override
    @Transactional
    public void deleteLoanApplication(Long id) {
        LoanApplication loanApplication = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LoanApplication", "id", id));
        
        loanApplicationRepository.delete(loanApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplicationResponseDto> findLoanApplicationsByCustomer(Customer customer) {
        List<LoanApplication> loanApplications = loanApplicationRepository.findByCustomer(customer);
        
        return loanApplications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplicationResponseDto> findLoanApplicationsByCustomerId(Long customerId) {
        List<LoanApplication> loanApplications = loanApplicationRepository.findByCustomerId(customerId);
        
        return loanApplications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplicationResponseDto> findLoanApplicationsByRiskLevel(String riskLevel) {
        List<LoanApplication> loanApplications = loanApplicationRepository.findByRiskLevel(riskLevel);
        
        return loanApplications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplicationResponseDto> findLoanApplicationsByDecision(String decision) {
        List<LoanApplication> loanApplications = loanApplicationRepository.findByDecision(decision);
        
        return loanApplications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplicationResponseDto> findLoanApplicationsByLoanPurpose(String loanPurpose) {
        List<LoanApplication> loanApplications = loanApplicationRepository.findByLoanPurposeContainingIgnoreCase(loanPurpose);
        
        return loanApplications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplicationResponseDto> findLoanApplicationsByLoanAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        List<LoanApplication> loanApplications = loanApplicationRepository.findByLoanAmountBetween(minAmount, maxAmount);
        
        return loanApplications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplicationResponseDto> findLoanApplicationsByMinimumRiskScore(Integer minRiskScore) {
        List<LoanApplication> loanApplications = loanApplicationRepository.findByRiskScoreGreaterThanEqual(minRiskScore);
        
        return loanApplications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplicationResponseDto> findLoanApplicationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<LoanApplication> loanApplications = loanApplicationRepository.findByCreatedAtBetween(startDate, endDate);
        
        return loanApplications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanApplicationResponseDto> findHighRiskHighValueApplications(Integer minRiskScore, BigDecimal minAmount) {
        List<LoanApplication> loanApplications = loanApplicationRepository.findHighRiskHighValueApplications(minRiskScore, minAmount);
        
        return loanApplications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LoanApplicationResponseDto evaluateLoanApplication(Long id) {
        LoanApplication loanApplication = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LoanApplication", "id", id));
        
        // This is a simplified risk evaluation logic
        // In a real application, this would use the ScoringRuleService to apply rules
        
        Customer customer = loanApplication.getCustomer();
        int riskScore = 0;
        
        // Credit score factor (lower credit score = higher risk)
        if (customer.getCreditScore() < 580) {
            riskScore += 40;
        } else if (customer.getCreditScore() < 670) {
            riskScore += 25;
        } else if (customer.getCreditScore() < 740) {
            riskScore += 10;
        }
        
        // Debt-to-income factor
        BigDecimal monthlyIncome = customer.getAnnualIncome().divide(new BigDecimal("12"), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal debtToIncomeRatio = customer.getExistingDebt().divide(monthlyIncome, 2, BigDecimal.ROUND_HALF_UP);
        
        if (debtToIncomeRatio.compareTo(new BigDecimal("0.5")) > 0) {
            riskScore += 30;
        } else if (debtToIncomeRatio.compareTo(new BigDecimal("0.3")) > 0) {
            riskScore += 15;
        }
        
        // Loan amount to income factor
        BigDecimal loanToIncomeRatio = loanApplication.getLoanAmount().divide(customer.getAnnualIncome(), 2, BigDecimal.ROUND_HALF_UP);
        
        if (loanToIncomeRatio.compareTo(new BigDecimal("0.5")) > 0) {
            riskScore += 20;
        } else if (loanToIncomeRatio.compareTo(new BigDecimal("0.3")) > 0) {
            riskScore += 10;
        }
        
        // Employment status factor
        if (!"EMPLOYED".equalsIgnoreCase(customer.getEmploymentStatus())) {
            riskScore += 15;
        }
        
        // Set risk score
        loanApplication.setRiskScore(riskScore);
        
        // Determine risk level
        if (riskScore >= 60) {
            loanApplication.setRiskLevel("HIGH");
            loanApplication.setDecision("REJECTED");
            loanApplication.setExplanation("High risk application due to credit score, debt ratio, and loan amount.");
        } else if (riskScore >= 30) {
            loanApplication.setRiskLevel("MEDIUM");
            loanApplication.setDecision("MANUAL_REVIEW");
            loanApplication.setExplanation("Medium risk application requires manual review by loan officer.");
        } else {
            loanApplication.setRiskLevel("LOW");
            loanApplication.setDecision("APPROVED");
            loanApplication.setExplanation("Low risk application approved automatically.");
        }
        
        LoanApplication evaluatedLoanApplication = loanApplicationRepository.save(loanApplication);
        
        return mapToDto(evaluatedLoanApplication);
    }

    /**
     * Map LoanApplication entity to LoanApplicationResponseDto
     */
    private LoanApplicationResponseDto mapToDto(LoanApplication loanApplication) {
        CustomerResponseDto customerDto = CustomerResponseDto.builder()
                .id(loanApplication.getCustomer().getId())
                .name(loanApplication.getCustomer().getName())
                .age(loanApplication.getCustomer().getAge())
                .annualIncome(loanApplication.getCustomer().getAnnualIncome())
                .creditScore(loanApplication.getCustomer().getCreditScore())
                .employmentStatus(loanApplication.getCustomer().getEmploymentStatus())
                .existingDebt(loanApplication.getCustomer().getExistingDebt())
                .createdAt(loanApplication.getCustomer().getCreatedAt())
                .updatedAt(loanApplication.getCustomer().getUpdatedAt())
                .build();
        
        return LoanApplicationResponseDto.builder()
                .id(loanApplication.getId())
                .customerId(loanApplication.getCustomer().getId())
                .customer(customerDto)
                .loanAmount(loanApplication.getLoanAmount())
                .loanPurpose(loanApplication.getLoanPurpose())
                .requestedTermMonths(loanApplication.getRequestedTermMonths())
                .riskScore(loanApplication.getRiskScore())
                .riskLevel(loanApplication.getRiskLevel())
                .decision(loanApplication.getDecision())
                .explanation(loanApplication.getExplanation())
                .createdAt(loanApplication.getCreatedAt())
                .updatedAt(loanApplication.getUpdatedAt())
                .build();
    }

    /**
     * Map LoanApplicationRequestDto to LoanApplication entity
     */
    private LoanApplication mapToEntity(LoanApplicationRequestDto loanApplicationRequestDto, Customer customer) {
        return LoanApplication.builder()
                .customer(customer)
                .loanAmount(loanApplicationRequestDto.getLoanAmount())
                .loanPurpose(loanApplicationRequestDto.getLoanPurpose())
                .requestedTermMonths(loanApplicationRequestDto.getRequestedTermMonths())
                .build();
    }
}