package com.loanrisk.controller;

import com.loanrisk.model.dto.ScoringRuleResponseDto;
import com.loanrisk.service.ScoringRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rules")
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
    @GetMapping
    public ResponseEntity<List<ScoringRuleResponseDto>> getActiveScoringRules() {
        List<ScoringRuleResponseDto> activeRules = scoringRuleService.findEnabledRules();
        return ResponseEntity.ok(activeRules);
    }
}