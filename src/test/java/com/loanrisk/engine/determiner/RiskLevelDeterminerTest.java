package com.loanrisk.engine.determiner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class RiskLevelDeterminerTest {

    private RiskLevelDeterminer determiner;

    @BeforeEach
    void setUp() {
        determiner = new RiskLevelDeterminer();
    }

    @ParameterizedTest
    @CsvSource({
            "0, LOW",
            "15, LOW",
            "30, LOW",
            "31, MEDIUM",
            "45, MEDIUM",
            "60, MEDIUM",
            "61, HIGH",
            "75, HIGH",
            "100, HIGH"
    })
    void testDetermineRiskLevel(int riskScore, String expectedRiskLevel) {
        String riskLevel = determiner.determineRiskLevel(riskScore);
        assertEquals(expectedRiskLevel, riskLevel);
    }

    @Test
    void testDetermineDecision() {
        assertEquals(RiskLevelDeterminer.DECISION_APPROVE, 
                determiner.determineDecision(RiskLevelDeterminer.RISK_LEVEL_LOW));
        
        assertEquals(RiskLevelDeterminer.DECISION_MANUAL_REVIEW, 
                determiner.determineDecision(RiskLevelDeterminer.RISK_LEVEL_MEDIUM));
        
        assertEquals(RiskLevelDeterminer.DECISION_REJECT, 
                determiner.determineDecision(RiskLevelDeterminer.RISK_LEVEL_HIGH));
    }

    @Test
    void testDetermineDecisionWithInvalidRiskLevel() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            determiner.determineDecision("INVALID_RISK_LEVEL");
        });
        
        assertTrue(exception.getMessage().contains("Unknown risk level"));
    }

    @ParameterizedTest
    @CsvSource({
            "0, APPROVE",
            "30, APPROVE",
            "31, MANUAL_REVIEW",
            "60, MANUAL_REVIEW",
            "61, REJECT",
            "100, REJECT"
    })
    void testDetermineDecisionFromScore(int riskScore, String expectedDecision) {
        String decision = determiner.determineDecisionFromScore(riskScore);
        assertEquals(expectedDecision, decision);
    }
}