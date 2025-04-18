package com.loanrisk.controller;

import com.loanrisk.model.dto.LoanApplicationRequestDto;
import com.loanrisk.model.dto.LoanApplicationResponseDto;
import com.loanrisk.service.LoanApplicationService;
import com.loanrisk.service.LoanEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loan")
@Tag(name = "Loan Application", description = "Loan application management APIs")
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
    @Operation(
        summary = "Submit a loan application",
        description = "Creates and evaluates a new loan application based on the customer's information and loan details"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Loan application created and evaluated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoanApplicationResponseDto.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "customerId": 1,
                      "customer": {
                        "id": 1,
                        "name": "John Doe",
                        "age": 35,
                        "annualIncome": 75000.00,
                        "creditScore": 720,
                        "employmentStatus": "EMPLOYED",
                        "existingDebt": 15000.00,
                        "createdAt": "2025-04-18T11:05:00",
                        "updatedAt": "2025-04-18T11:05:00"
                      },
                      "loanAmount": 25000.00,
                      "loanPurpose": "HOME_IMPROVEMENT",
                      "requestedTermMonths": 36,
                      "riskScore": 65,
                      "riskLevel": "MEDIUM",
                      "decision": "APPROVED",
                      "explanation": "Loan approved with standard interest rate",
                      "createdAt": "2025-04-18T11:05:00",
                      "updatedAt": "2025-04-18T11:05:00"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "timestamp": "2025-04-18T11:05:00",
                      "status": 400,
                      "error": "Bad Request",
                      "message": "Validation failed",
                      "path": "/loan/apply"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Customer not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "timestamp": "2025-04-18T11:05:00",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Customer with ID 1 not found",
                      "path": "/loan/apply"
                    }
                    """
                )
            )
        )
    })
    @PostMapping("/apply")
    public ResponseEntity<LoanApplicationResponseDto> submitLoanApplication(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Loan application information",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LoanApplicationRequestDto.class),
                    examples = @ExampleObject(
                        value = """
                        {
                          "customerId": 1,
                          "loanAmount": 25000.00,
                          "loanPurpose": "HOME_IMPROVEMENT",
                          "requestedTermMonths": 36
                        }
                        """
                    )
                )
            )
            LoanApplicationRequestDto loanApplicationRequestDto) {
        
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
    @Operation(
        summary = "Get a loan application by ID",
        description = "Retrieves a loan application by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Loan application found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoanApplicationResponseDto.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "customerId": 1,
                      "customer": {
                        "id": 1,
                        "name": "John Doe",
                        "age": 35,
                        "annualIncome": 75000.00,
                        "creditScore": 720,
                        "employmentStatus": "EMPLOYED",
                        "existingDebt": 15000.00,
                        "createdAt": "2025-04-18T11:05:00",
                        "updatedAt": "2025-04-18T11:05:00"
                      },
                      "loanAmount": 25000.00,
                      "loanPurpose": "HOME_IMPROVEMENT",
                      "requestedTermMonths": 36,
                      "riskScore": 65,
                      "riskLevel": "MEDIUM",
                      "decision": "APPROVED",
                      "explanation": "Loan approved with standard interest rate",
                      "createdAt": "2025-04-18T11:05:00",
                      "updatedAt": "2025-04-18T11:05:00"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Loan application not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = """
                    {
                      "timestamp": "2025-04-18T11:05:00",
                      "status": 404,
                      "error": "Not Found",
                      "message": "Loan application with ID 1 not found",
                      "path": "/loan/1"
                    }
                    """
                )
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<LoanApplicationResponseDto> getLoanApplicationById(
            @Parameter(description = "Loan application ID", required = true, example = "1")
            @PathVariable Long id) {
        LoanApplicationResponseDto loanApplication = loanApplicationService.getLoanApplicationById(id);
        return ResponseEntity.ok(loanApplication);
    }
}