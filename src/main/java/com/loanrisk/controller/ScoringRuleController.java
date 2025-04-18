package com.loanrisk.controller;

import com.loanrisk.model.dto.ScoringRuleResponseDto;
import com.loanrisk.service.ScoringRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rules")
@Tag(name = "Scoring Rules", description = "Scoring rules management APIs")
public class ScoringRuleController {

    private final ScoringRuleService scoringRuleService;

    @Autowired
    public ScoringRuleController(ScoringRuleService scoringRuleService) {
        this.scoringRuleService = scoringRuleService;
    }

    /**
     * Retrieve all active scoring rules
     *
     * @return List of active scoring rules
     */
    @Operation(
        summary = "Get all active scoring rules",
        description = "Retrieves all enabled scoring rules used for loan risk evaluation"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of active scoring rules",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ScoringRuleResponseDto.class),
                examples = @ExampleObject(
                    value = """
                    [
                      {
                        "id": 1,
                        "name": "High Credit Score",
                        "field": "creditScore",
                        "operator": "GREATER_THAN_OR_EQUAL",
                        "ruleValue": "720",
                        "riskPoints": -20,
                        "priority": 1,
                        "enabled": true,
                        "createdAt": "2025-04-18T11:05:00",
                        "updatedAt": "2025-04-18T11:05:00"
                      },
                      {
                        "id": 2,
                        "name": "Low Credit Score",
                        "field": "creditScore",
                        "operator": "LESS_THAN",
                        "ruleValue": "580",
                        "riskPoints": 30,
                        "priority": 2,
                        "enabled": true,
                        "createdAt": "2025-04-18T11:05:00",
                        "updatedAt": "2025-04-18T11:05:00"
                      }
                    ]
                    """
                )
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<ScoringRuleResponseDto>> getActiveScoringRules() {
        List<ScoringRuleResponseDto> activeRules = scoringRuleService.findEnabledRules();
        return ResponseEntity.ok(activeRules);
    }
}