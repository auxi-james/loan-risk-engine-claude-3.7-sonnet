-- Create Customer table
CREATE TABLE customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    annual_income DECIMAL(19, 2) NOT NULL,
    credit_score INT NOT NULL,
    employment_status VARCHAR(50) NOT NULL,
    existing_debt DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create LoanApplication table
CREATE TABLE loan_application (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    loan_amount DECIMAL(19, 2) NOT NULL,
    loan_purpose VARCHAR(100) NOT NULL,
    requested_term_months INT NOT NULL,
    risk_score INT,
    risk_level VARCHAR(50),
    decision VARCHAR(50),
    explanation TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- Create ScoringRule table
CREATE TABLE scoring_rule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    field VARCHAR(100) NOT NULL,
    operator VARCHAR(50) NOT NULL,
    rule_value VARCHAR(255) NOT NULL,
    risk_points INT NOT NULL,
    priority INT NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_customer_credit_score ON customer(credit_score);
CREATE INDEX idx_loan_application_customer_id ON loan_application(customer_id);
CREATE INDEX idx_loan_application_risk_level ON loan_application(risk_level);
CREATE INDEX idx_scoring_rule_field ON scoring_rule(field);
CREATE INDEX idx_scoring_rule_enabled ON scoring_rule(enabled);
CREATE INDEX idx_scoring_rule_priority ON scoring_rule(priority);