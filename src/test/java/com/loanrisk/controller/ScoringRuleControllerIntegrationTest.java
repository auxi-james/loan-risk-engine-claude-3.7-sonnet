package com.loanrisk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanrisk.model.dto.ScoringRuleResponseDto;
import com.loanrisk.service.ScoringRuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScoringRuleController.class)
public class ScoringRuleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ScoringRuleService scoringRuleService;

    @Test
    public void testGetActiveScoringRules_Success() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        
        ScoringRuleResponseDto rule1 = ScoringRuleResponseDto.builder()
                .id(1L)
                .name("Low Credit Score")
                .field("creditScore")
                .operator("LESS_THAN")
                .ruleValue("600")
                .riskPoints(30)
                .priority(1)
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        ScoringRuleResponseDto rule2 = ScoringRuleResponseDto.builder()
                .id(2L)
                .name("High Debt-to-Income Ratio")
                .field("debtToIncomeRatio")
                .operator("GREATER_THAN")
                .ruleValue("0.5")
                .riskPoints(25)
                .priority(2)
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        List<ScoringRuleResponseDto> activeRules = Arrays.asList(rule1, rule2);

        when(scoringRuleService.findEnabledRules()).thenReturn(activeRules);

        // Act & Assert
        mockMvc.perform(get("/rules")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Low Credit Score"))
                .andExpect(jsonPath("$[0].field").value("creditScore"))
                .andExpect(jsonPath("$[0].operator").value("LESS_THAN"))
                .andExpect(jsonPath("$[0].ruleValue").value("600"))
                .andExpect(jsonPath("$[0].riskPoints").value(30))
                .andExpect(jsonPath("$[0].priority").value(1))
                .andExpect(jsonPath("$[0].enabled").value(true))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("High Debt-to-Income Ratio"))
                .andExpect(jsonPath("$[1].field").value("debtToIncomeRatio"))
                .andExpect(jsonPath("$[1].operator").value("GREATER_THAN"))
                .andExpect(jsonPath("$[1].ruleValue").value("0.5"))
                .andExpect(jsonPath("$[1].riskPoints").value(25))
                .andExpect(jsonPath("$[1].priority").value(2))
                .andExpect(jsonPath("$[1].enabled").value(true));
    }

    @Test
    public void testGetActiveScoringRules_EmptyList() throws Exception {
        // Arrange
        List<ScoringRuleResponseDto> emptyList = Arrays.asList();

        when(scoringRuleService.findEnabledRules()).thenReturn(emptyList);

        // Act & Assert
        mockMvc.perform(get("/rules")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}