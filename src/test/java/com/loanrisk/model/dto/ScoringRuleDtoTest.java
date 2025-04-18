package com.loanrisk.model.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ScoringRuleDtoTest {

    @Test
    void testScoringRuleResponseDto() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        
        // When
        ScoringRuleResponseDto dto = ScoringRuleResponseDto.builder()
                .id(1L)
                .name("Credit too low")
                .field("creditScore")
                .operator("<")
                .ruleValue("600")
                .riskPoints(30)
                .priority(1)
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // Then
        assertEquals(1L, dto.getId());
        assertEquals("Credit too low", dto.getName());
        assertEquals("creditScore", dto.getField());
        assertEquals("<", dto.getOperator());
        assertEquals("600", dto.getRuleValue());
        assertEquals(30, dto.getRiskPoints());
        assertEquals(1, dto.getPriority());
        assertTrue(dto.getEnabled());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }

    @Test
    void testScoringRuleResponseDtoNoArgsConstructor() {
        // When
        ScoringRuleResponseDto dto = new ScoringRuleResponseDto();
        
        // Then
        assertNull(dto.getId());
        assertNull(dto.getName());
        assertNull(dto.getField());
        assertNull(dto.getOperator());
        assertNull(dto.getRuleValue());
        assertNull(dto.getRiskPoints());
        assertNull(dto.getPriority());
        assertNull(dto.getEnabled());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getUpdatedAt());
    }

    @Test
    void testScoringRuleResponseDtoSettersAndGetters() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        ScoringRuleResponseDto dto = new ScoringRuleResponseDto();
        
        // When
        dto.setId(1L);
        dto.setName("Credit too low");
        dto.setField("creditScore");
        dto.setOperator("<");
        dto.setRuleValue("600");
        dto.setRiskPoints(30);
        dto.setPriority(1);
        dto.setEnabled(true);
        dto.setCreatedAt(now);
        dto.setUpdatedAt(now);
        
        // Then
        assertEquals(1L, dto.getId());
        assertEquals("Credit too low", dto.getName());
        assertEquals("creditScore", dto.getField());
        assertEquals("<", dto.getOperator());
        assertEquals("600", dto.getRuleValue());
        assertEquals(30, dto.getRiskPoints());
        assertEquals(1, dto.getPriority());
        assertTrue(dto.getEnabled());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(now, dto.getUpdatedAt());
    }
}