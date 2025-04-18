package com.loanrisk.engine.determiner;

import org.springframework.stereotype.Component;

/**
 * Class for determining risk level and decision based on risk score
 */
@Component
public class RiskLevelDeterminer {

    // Risk level constants
    public static final String RISK_LEVEL_LOW = "LOW";
    public static final String RISK_LEVEL_MEDIUM = "MEDIUM";
    public static final String RISK_LEVEL_HIGH = "HIGH";
    
    // Decision constants
    public static final String DECISION_APPROVE = "APPROVE";
    public static final String DECISION_MANUAL_REVIEW = "MANUAL_REVIEW";
    public static final String DECISION_REJECT = "REJECT";
    
    // Score thresholds
    private static final int LOW_RISK_MAX_SCORE = 30;
    private static final int MEDIUM_RISK_MAX_SCORE = 60;
    
    /**
     * Determine the risk level based on the risk score
     * 
     * @param riskScore the calculated risk score
     * @return the risk level (LOW, MEDIUM, or HIGH)
     */
    public String determineRiskLevel(int riskScore) {
        if (riskScore <= LOW_RISK_MAX_SCORE) {
            return RISK_LEVEL_LOW;
        } else if (riskScore <= MEDIUM_RISK_MAX_SCORE) {
            return RISK_LEVEL_MEDIUM;
        } else {
            return RISK_LEVEL_HIGH;
        }
    }
    
    /**
     * Determine the decision based on the risk level
     * 
     * @param riskLevel the determined risk level
     * @return the decision (APPROVE, MANUAL_REVIEW, or REJECT)
     */
    public String determineDecision(String riskLevel) {
        switch (riskLevel) {
            case RISK_LEVEL_LOW:
                return DECISION_APPROVE;
            case RISK_LEVEL_MEDIUM:
                return DECISION_MANUAL_REVIEW;
            case RISK_LEVEL_HIGH:
                return DECISION_REJECT;
            default:
                throw new IllegalArgumentException("Unknown risk level: " + riskLevel);
        }
    }
    
    /**
     * Determine the decision directly from the risk score
     * 
     * @param riskScore the calculated risk score
     * @return the decision (APPROVE, MANUAL_REVIEW, or REJECT)
     */
    public String determineDecisionFromScore(int riskScore) {
        String riskLevel = determineRiskLevel(riskScore);
        return determineDecision(riskLevel);
    }
}