package com.loanrisk.migration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class FlywayMigrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testTablesCreated() {
        // Check if Customer table exists and has the expected structure
        Integer customerCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'CUSTOMER'", Integer.class);
        assertTrue(customerCount > 0, "Customer table should exist");

        // Check if LoanApplication table exists and has the expected structure
        Integer loanApplicationCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'LOAN_APPLICATION'", Integer.class);
        assertTrue(loanApplicationCount > 0, "LoanApplication table should exist");

        // Check if ScoringRule table exists and has the expected structure
        Integer scoringRuleCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'SCORING_RULE'", Integer.class);
        assertTrue(scoringRuleCount > 0, "ScoringRule table should exist");
    }

    @Test
    public void testInitialScoringRulesInserted() {
        // Check if initial scoring rules are inserted
        Integer scoringRuleCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM SCORING_RULE", Integer.class);
        assertEquals(6, scoringRuleCount, "There should be 6 initial scoring rules");

        // Check specific rules
        Integer creditTooLowCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM SCORING_RULE WHERE NAME = 'Credit too low'", Integer.class);
        assertEquals(1, creditTooLowCount, "Credit too low rule should exist");

        Integer vacationLoanCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM SCORING_RULE WHERE NAME = 'Vacation loan'", Integer.class);
        assertEquals(1, vacationLoanCount, "Vacation loan rule should exist");
    }
}