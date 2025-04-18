package com.loanrisk.controller;

import com.loanrisk.model.dto.LoanApplicationRequestDto;
import com.loanrisk.model.dto.LoanApplicationResponseDto;
import com.loanrisk.service.LoanApplicationService;
import com.loanrisk.service.LoanEvaluationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    @Autowired
    public LoanApplicationController(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    /**
     * Submit a loan application for evaluation
     * 
     * @param loanApplicationRequestDto The loan application data
     * @return The created and evaluated loan application
     */
    @PostMapping("/apply")
    public ResponseEntity<LoanApplicationResponseDto> submitLoanApplication(
            @Valid @RequestBody LoanApplicationRequestDto loanApplicationRequestDto) {
        
        // Create the loan application
        LoanApplicationResponseDto createdApplication = loanApplicationService.createLoanApplication(loanApplicationRequestDto);
        
        // Evaluate the loan application
        LoanApplicationResponseDto evaluatedApplication = loanApplicationService.evaluateLoanApplication(createdApplication.getId());
        
        return new ResponseEntity<>(evaluatedApplication, HttpStatus.CREATED);
    }

    /**
     * Get a loan application by ID
     * 
     * @param id The loan application ID
     * @return The loan application data
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoanApplicationResponseDto> getLoanApplicationById(@PathVariable Long id) {
        LoanApplicationResponseDto loanApplication = loanApplicationService.getLoanApplicationById(id);
        return ResponseEntity.ok(loanApplication);
    }
}