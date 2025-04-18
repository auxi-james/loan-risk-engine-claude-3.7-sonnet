package com.loanrisk.repository;

import com.loanrisk.model.entity.ScoringRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ScoringRuleRepositoryTest {

    @Autowired
    private ScoringRuleRepository scoringRuleRepository;

    @BeforeEach
    public void setup() {
        // Clear existing rules
        scoringRuleRepository.deleteAll();
        
        // Create test scoring rules
        ScoringRule rule1 = ScoringRule.builder()
                .name("Low Credit Score")
                .field("creditScore")
                .operator("LESS_THAN")
                .ruleValue("600")
                .riskPoints(30)
                .priority(1)
                .enabled(true)
                .build();

        ScoringRule rule2 = ScoringRule.builder()
                .name("High Debt-to-Income")
                .field("debtToIncome")
                .operator("GREATER_THAN")
                .ruleValue("0.5")
                .riskPoints(25)
                .priority(2)
                .enabled(true)
                .build();

        ScoringRule rule3 = ScoringRule.builder()
                .name("Unemployed Status")
                .field("employmentStatus")
                .operator("EQUALS")
                .ruleValue("UNEMPLOYED")
                .riskPoints(40)
                .priority(1)
                .enabled(true)
                .build();

        ScoringRule rule4 = ScoringRule.builder()
                .name("Low Income")
                .field("annualIncome")
                .operator("LESS_THAN")
                .ruleValue("30000")
                .riskPoints(20)
                .priority(3)
                .enabled(false)
                .build();

        ScoringRule rule5 = ScoringRule.builder()
                .name("High Loan Amount")
                .field("loanAmount")
                .operator("GREATER_THAN")
                .ruleValue("100000")
                .riskPoints(15)
                .priority(4)
                .enabled(true)
                .build();

        // Save all rules
        scoringRuleRepository.saveAll(List.of(rule1, rule2, rule3, rule4, rule5));
    }

    @Test
    public void testSaveScoringRule() {
        // Create a scoring rule
        ScoringRule rule = ScoringRule.builder()
                .name("Test Rule")
                .field("testField")
                .operator("EQUALS")
                .ruleValue("testValue")
                .riskPoints(10)
                .priority(5)
                .enabled(true)
                .build();

        // Save the rule
        ScoringRule savedRule = scoringRuleRepository.save(rule);

        // Verify the rule was saved with an ID
        assertThat(savedRule.getId()).isNotNull();
        assertThat(savedRule.getName()).isEqualTo("Test Rule");
        assertThat(savedRule.getCreatedAt()).isNotNull();
        assertThat(savedRule.getUpdatedAt()).isNotNull();
    }

    @Test
    public void testFindByEnabledTrue() {
        // Test finding enabled rules
        List<ScoringRule> enabledRules = scoringRuleRepository.findByEnabledTrue();
        assertThat(enabledRules).hasSize(4);
        assertThat(enabledRules).extracting(ScoringRule::getName)
                .containsExactlyInAnyOrder("Low Credit Score", "High Debt-to-Income", "Unemployed Status", "High Loan Amount");
    }

    @Test
    public void testFindByEnabledFalse() {
        // Test finding disabled rules
        List<ScoringRule> disabledRules = scoringRuleRepository.findByEnabledFalse();
        assertThat(disabledRules).hasSize(1);
        assertThat(disabledRules.get(0).getName()).isEqualTo("Low Income");
    }

    @Test
    public void testFindByField() {
        // Test finding rules by field
        List<ScoringRule> creditScoreRules = scoringRuleRepository.findByField("creditScore");
        assertThat(creditScoreRules).hasSize(1);
        assertThat(creditScoreRules.get(0).getName()).isEqualTo("Low Credit Score");
    }

    @Test
    public void testFindByPriorityLessThanEqualOrderByPriorityAsc() {
        // Test finding rules with priority <= 2, ordered by priority
        List<ScoringRule> highPriorityRules = scoringRuleRepository.findByPriorityLessThanEqualOrderByPriorityAsc(2);
        assertThat(highPriorityRules).hasSize(3);
        assertThat(highPriorityRules).extracting(ScoringRule::getPriority)
                .containsExactly(1, 1, 2);
    }

    @Test
    public void testFindByPriorityBetweenOrderByPriorityAsc() {
        // Test finding rules with priority between 2 and 4, ordered by priority
        List<ScoringRule> mediumPriorityRules = scoringRuleRepository.findByPriorityBetweenOrderByPriorityAsc(2, 4);
        assertThat(mediumPriorityRules).hasSize(3);
        assertThat(mediumPriorityRules).extracting(ScoringRule::getName)
                .containsExactly("High Debt-to-Income", "Low Income", "High Loan Amount");
    }

    @Test
    public void testFindByFieldAndEnabledTrue() {
        // Test finding enabled rules by field
        List<ScoringRule> enabledCreditScoreRules = scoringRuleRepository.findByFieldAndEnabledTrue("creditScore");
        assertThat(enabledCreditScoreRules).hasSize(1);
        assertThat(enabledCreditScoreRules.get(0).getName()).isEqualTo("Low Credit Score");
        assertThat(enabledCreditScoreRules.get(0).getEnabled()).isTrue();

        // Test finding enabled rules for a field with disabled rules
        List<ScoringRule> enabledIncomeRules = scoringRuleRepository.findByFieldAndEnabledTrue("annualIncome");
        assertThat(enabledIncomeRules).isEmpty();
    }

    @Test
    public void testFindByEnabledTrueOrderByPriorityAsc() {
        // Test finding enabled rules ordered by priority
        List<ScoringRule> orderedEnabledRules = scoringRuleRepository.findByEnabledTrueOrderByPriorityAsc();
        assertThat(orderedEnabledRules).hasSize(4);
        assertThat(orderedEnabledRules).extracting(ScoringRule::getPriority)
                .containsExactly(1, 1, 2, 4);
    }

    @Test
    public void testFindByNameContainingIgnoreCase() {
        // Test finding rules by name containing "income" (case-insensitive)
        List<ScoringRule> incomeRules = scoringRuleRepository.findByNameContainingIgnoreCase("income");
        assertThat(incomeRules).hasSize(2);
        assertThat(incomeRules).extracting(ScoringRule::getName)
                .containsExactlyInAnyOrder("High Debt-to-Income", "Low Income");

        // Test finding rules by name containing "HIGH" (case-insensitive)
        List<ScoringRule> highRules = scoringRuleRepository.findByNameContainingIgnoreCase("HIGH");
        assertThat(highRules).hasSize(2);
        assertThat(highRules).extracting(ScoringRule::getName)
                .containsExactlyInAnyOrder("High Debt-to-Income", "High Loan Amount");
    }

    @Test
    public void testFindByOperator() {
        // Test finding rules by operator
        List<ScoringRule> lessThanRules = scoringRuleRepository.findByOperator("LESS_THAN");
        assertThat(lessThanRules).hasSize(2);
        assertThat(lessThanRules).extracting(ScoringRule::getName)
                .containsExactlyInAnyOrder("Low Credit Score", "Low Income");
    }

    @Test
    public void testFindByRiskPointsGreaterThanEqual() {
        // Test finding rules with risk points >= 30
        List<ScoringRule> highRiskRules = scoringRuleRepository.findByRiskPointsGreaterThanEqual(30);
        assertThat(highRiskRules).hasSize(2);
        assertThat(highRiskRules).extracting(ScoringRule::getName)
                .containsExactlyInAnyOrder("Low Credit Score", "Unemployed Status");
    }

    @Test
    public void testFindHighPriorityEnabledRules() {
        // Test custom query to find high priority enabled rules (priority <= 2)
        List<ScoringRule> highPriorityEnabledRules = scoringRuleRepository.findHighPriorityEnabledRules(2);
        assertThat(highPriorityEnabledRules).hasSize(3);
        assertThat(highPriorityEnabledRules).extracting(ScoringRule::getName)
                .containsExactlyInAnyOrder("Low Credit Score", "High Debt-to-Income", "Unemployed Status");
        assertThat(highPriorityEnabledRules).allMatch(ScoringRule::getEnabled);
    }
}