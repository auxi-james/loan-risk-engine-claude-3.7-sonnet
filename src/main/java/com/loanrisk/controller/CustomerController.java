package com.loanrisk.controller;

import com.loanrisk.model.dto.CustomerRequestDto;
import com.loanrisk.model.dto.CustomerResponseDto;
import com.loanrisk.service.CustomerService;
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
@RequestMapping("/customers")
@Tag(name = "Customer", description = "Customer management APIs")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Create a new customer
     *
     * @param customerRequestDto The customer data
     * @return The created customer
     */
    @Operation(
        summary = "Create a new customer",
        description = "Creates a new customer with the provided information"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Customer created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CustomerResponseDto.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "name": "John Doe",
                      "age": 35,
                      "annualIncome": 75000.00,
                      "creditScore": 720,
                      "employmentStatus": "EMPLOYED",
                      "existingDebt": 15000.00,
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
                      "path": "/customers"
                    }
                    """
                )
            )
        )
    })
    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Customer information",
                required = true,
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CustomerRequestDto.class),
                    examples = @ExampleObject(
                        value = """
                        {
                          "name": "John Doe",
                          "age": 35,
                          "annualIncome": 75000.00,
                          "creditScore": 720,
                          "employmentStatus": "EMPLOYED",
                          "existingDebt": 15000.00
                        }
                        """
                    )
                )
            )
            CustomerRequestDto customerRequestDto) {
        CustomerResponseDto createdCustomer = customerService.createCustomer(customerRequestDto);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    /**
     * Get a customer by ID
     *
     * @param id The customer ID
     * @return The customer data
     */
    @Operation(
        summary = "Get a customer by ID",
        description = "Retrieves a customer by their unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Customer found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CustomerResponseDto.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "name": "John Doe",
                      "age": 35,
                      "annualIncome": 75000.00,
                      "creditScore": 720,
                      "employmentStatus": "EMPLOYED",
                      "existingDebt": 15000.00,
                      "createdAt": "2025-04-18T11:05:00",
                      "updatedAt": "2025-04-18T11:05:00"
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
                      "path": "/customers/1"
                    }
                    """
                )
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(
            @Parameter(description = "Customer ID", required = true, example = "1")
            @PathVariable Long id) {
        CustomerResponseDto customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }
}