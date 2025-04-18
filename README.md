# Loan Risk Scoring Engine

A Spring Boot application for evaluating loan applications based on risk scoring rules.

## Project Overview

The Loan Risk Scoring Engine is a robust application designed to evaluate loan applications based on configurable risk scoring rules. It provides a comprehensive API for managing customers, loan applications, and scoring rules.

## Project Structure

```
loan-risk-engine/
├── src/
│   ├── main/
│   │   ├── java/com/loanrisk/
│   │   │   ├── config/             # Configuration classes
│   │   │   ├── controller/         # REST API controllers
│   │   │   ├── engine/             # Rule engine implementation
│   │   │   │   ├── calculator/     # Derived field calculators
│   │   │   │   ├── determiner/     # Risk level determination
│   │   │   │   ├── evaluator/      # Rule evaluation logic
│   │   │   │   └── factory/        # Factory for rule evaluators
│   │   │   ├── exception/          # Exception handling
│   │   │   ├── model/              # Data models
│   │   │   │   ├── dto/            # Data transfer objects
│   │   │   │   └── entity/         # Database entities
│   │   │   ├── repository/         # Data repositories
│   │   │   ├── service/            # Business logic services
│   │   │   └── LoanRiskApplication.java  # Application entry point
│   │   └── resources/
│   │       ├── application.properties    # Main application properties
│   │       ├── application-dev.properties # Development environment properties
│   │       ├── application-prod.properties # Production environment properties
│   │       ├── application-test.properties # Test environment properties
│   │       └── db/migration/      # Flyway database migrations
│   └── test/
│       └── java/com/loanrisk/     # Test classes
├── pom.xml                        # Maven configuration
└── README.md                      # This file
```

## Technologies Used

- Java 21
- Spring Boot 3.4.4
- Spring Data JPA
- H2 Database (for development and testing)
- PostgreSQL (for production)
- Flyway for database migrations
- Springdoc OpenAPI for API documentation
- JUnit 5 for testing

## How to Run the Application

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/loan-risk-engine.git
   cd loan-risk-engine
   ```

2. Build the application:
   ```
   ./mvnw clean install
   ```

3. Run the application:
   ```
   ./mvnw spring-boot:run
   ```

The application will start on port 8080 by default. You can access the API at `http://localhost:8080`.

### Profiles

The application supports multiple profiles:

- `dev` (default): Uses H2 in-memory database
- `test`: Used for running tests
- `prod`: Configured for production environment with PostgreSQL

To run with a specific profile:

```
./mvnw spring-boot:run -Dspring.profiles.active=prod
```

## How to Run Tests

Run all tests:

```
./mvnw test
```

Run a specific test class:

```
./mvnw test -Dtest=LoanApplicationServiceTest
```

## API Documentation

The API documentation is available via Swagger UI when the application is running:

```
http://localhost:8080/swagger-ui.html
```

API documentation is also available in JSON format:

```
http://localhost:8080/api-docs
```

### API Endpoints

#### Customer API

- `POST /customers` - Create a new customer
- `GET /customers/{id}` - Get a customer by ID

#### Loan Application API

- `POST /loan/apply` - Submit a loan application for evaluation
- `GET /loan/{id}` - Get a loan application by ID

#### Scoring Rules API

- `GET /rules` - Get all active scoring rules

## Rule Engine

The Loan Risk Scoring Engine uses a flexible rule-based system to evaluate loan applications. The rule engine consists of several components:

### Rule Evaluators

The engine supports various rule evaluators for different types of comparisons:

- Equals/NotEquals
- GreaterThan/LessThan
- GreaterThanOrEqual/LessThanOrEqual
- Contains/NotContains
- StartsWith/EndsWith

### Rule Evaluation Process

1. **Data Collection**: The engine collects data from the loan application and customer profile.
2. **Derived Field Calculation**: Additional fields are calculated based on the collected data (e.g., debt-to-income ratio).
3. **Rule Evaluation**: Each rule is evaluated against the data.
4. **Risk Score Calculation**: A risk score is calculated based on the evaluated rules.
5. **Risk Level Determination**: The risk level (LOW, MEDIUM, HIGH) is determined based on the risk score.
6. **Decision Making**: A loan decision (APPROVED, APPROVED_WITH_CONDITIONS, DENIED) is made based on the risk level.

### Configuring Rules

Rules are stored in the database and can be configured through the API. Each rule consists of:

- Field name
- Operator
- Value to compare against
- Risk points (positive for higher risk, negative for lower risk)
- Priority (for rule execution order)
- Enabled flag

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.