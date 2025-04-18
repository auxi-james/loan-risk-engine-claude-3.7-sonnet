-- Insert initial scoring rules

-- Rule 1: Credit too low
INSERT INTO scoring_rule (name, field, operator, rule_value, risk_points, priority, enabled)
VALUES ('Credit too low', 'creditScore', '<', '600', 30, 1, true);

-- Rule 2: Credit average
INSERT INTO scoring_rule (name, field, operator, rule_value, risk_points, priority, enabled)
VALUES ('Credit average', 'creditScore', '<', '700', 15, 2, true);

-- Rule 3: Loan-to-income high
INSERT INTO scoring_rule (name, field, operator, rule_value, risk_points, priority, enabled)
VALUES ('Loan-to-income high', 'loanRatio', '>', '0.5', 25, 1, true);

-- Rule 4: Debt is high
INSERT INTO scoring_rule (name, field, operator, rule_value, risk_points, priority, enabled)
VALUES ('Debt is high', 'existingDebtRatio', '>', '0.4', 20, 2, true);

-- Rule 5: Too young
INSERT INTO scoring_rule (name, field, operator, rule_value, risk_points, priority, enabled)
VALUES ('Too young', 'age', '<', '21', 20, 3, true);

-- Rule 6: Vacation loan
INSERT INTO scoring_rule (name, field, operator, rule_value, risk_points, priority, enabled)
VALUES ('Vacation loan', 'loanPurpose', '==', 'vacation', 10, 4, true);