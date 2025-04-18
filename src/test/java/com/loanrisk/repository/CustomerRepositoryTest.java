package com.loanrisk.repository;

import com.loanrisk.model.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testSaveCustomer() {
        // Create a customer
        Customer customer = Customer.builder()
                .name("John Doe")
                .age(35)
                .annualIncome(new BigDecimal("75000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("15000.00"))
                .build();

        // Save the customer
        Customer savedCustomer = customerRepository.save(customer);

        // Verify the customer was saved with an ID
        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getName()).isEqualTo("John Doe");
        assertThat(savedCustomer.getCreatedAt()).isNotNull();
        assertThat(savedCustomer.getUpdatedAt()).isNotNull();
    }

    @Test
    public void testFindByNameContainingIgnoreCase() {
        // Create and save customers
        Customer customer1 = Customer.builder()
                .name("John Smith")
                .age(30)
                .annualIncome(new BigDecimal("65000.00"))
                .creditScore(700)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("10000.00"))
                .build();

        Customer customer2 = Customer.builder()
                .name("Jane Smith")
                .age(28)
                .annualIncome(new BigDecimal("70000.00"))
                .creditScore(730)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("5000.00"))
                .build();

        Customer customer3 = Customer.builder()
                .name("Robert Johnson")
                .age(45)
                .annualIncome(new BigDecimal("90000.00"))
                .creditScore(750)
                .employmentStatus("SELF_EMPLOYED")
                .existingDebt(new BigDecimal("20000.00"))
                .build();

        customerRepository.saveAll(List.of(customer1, customer2, customer3));

        // Test finding by name containing "Smith" (case-insensitive)
        List<Customer> smithCustomers = customerRepository.findByNameContainingIgnoreCase("smith");
        assertThat(smithCustomers).hasSize(2);
        assertThat(smithCustomers).extracting(Customer::getName)
                .containsExactlyInAnyOrder("John Smith", "Jane Smith");

        // Test finding by name containing "john" (case-insensitive)
        List<Customer> johnCustomers = customerRepository.findByNameContainingIgnoreCase("john ");
        assertThat(johnCustomers).hasSize(1);
        assertThat(johnCustomers.get(0).getName()).isEqualTo("John Smith");
    }

    @Test
    public void testFindByCreditScoreBetween() {
        // Create and save customers
        Customer customer1 = Customer.builder()
                .name("John Smith")
                .age(30)
                .annualIncome(new BigDecimal("65000.00"))
                .creditScore(680)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("10000.00"))
                .build();

        Customer customer2 = Customer.builder()
                .name("Jane Smith")
                .age(28)
                .annualIncome(new BigDecimal("70000.00"))
                .creditScore(720)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("5000.00"))
                .build();

        Customer customer3 = Customer.builder()
                .name("Robert Johnson")
                .age(45)
                .annualIncome(new BigDecimal("90000.00"))
                .creditScore(780)
                .employmentStatus("SELF_EMPLOYED")
                .existingDebt(new BigDecimal("20000.00"))
                .build();

        customerRepository.saveAll(List.of(customer1, customer2, customer3));

        // Test finding by credit score between 700 and 750
        List<Customer> customers = customerRepository.findByCreditScoreBetween(700, 750);
        assertThat(customers).hasSize(1);
        assertThat(customers.get(0).getName()).isEqualTo("Jane Smith");
        assertThat(customers.get(0).getCreditScore()).isEqualTo(720);
    }

    @Test
    public void testFindByEmploymentStatus() {
        // Create and save customers
        Customer customer1 = Customer.builder()
                .name("John Smith")
                .age(30)
                .annualIncome(new BigDecimal("65000.00"))
                .creditScore(700)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("10000.00"))
                .build();

        Customer customer2 = Customer.builder()
                .name("Jane Smith")
                .age(28)
                .annualIncome(new BigDecimal("70000.00"))
                .creditScore(730)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("5000.00"))
                .build();

        Customer customer3 = Customer.builder()
                .name("Robert Johnson")
                .age(45)
                .annualIncome(new BigDecimal("90000.00"))
                .creditScore(750)
                .employmentStatus("SELF_EMPLOYED")
                .existingDebt(new BigDecimal("20000.00"))
                .build();

        customerRepository.saveAll(List.of(customer1, customer2, customer3));

        // Test finding by employment status
        List<Customer> employedCustomers = customerRepository.findByEmploymentStatus("EMPLOYED");
        assertThat(employedCustomers).hasSize(2);
        assertThat(employedCustomers).extracting(Customer::getName)
                .containsExactlyInAnyOrder("John Smith", "Jane Smith");

        List<Customer> selfEmployedCustomers = customerRepository.findByEmploymentStatus("SELF_EMPLOYED");
        assertThat(selfEmployedCustomers).hasSize(1);
        assertThat(selfEmployedCustomers.get(0).getName()).isEqualTo("Robert Johnson");
    }

    @Test
    public void testFindByAnnualIncomeGreaterThanEqual() {
        // Create and save customers
        Customer customer1 = Customer.builder()
                .name("John Smith")
                .age(30)
                .annualIncome(new BigDecimal("65000.00"))
                .creditScore(700)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("10000.00"))
                .build();

        Customer customer2 = Customer.builder()
                .name("Jane Smith")
                .age(28)
                .annualIncome(new BigDecimal("70000.00"))
                .creditScore(730)
                .employmentStatus("EMPLOYED")
                .existingDebt(new BigDecimal("5000.00"))
                .build();

        Customer customer3 = Customer.builder()
                .name("Robert Johnson")
                .age(45)
                .annualIncome(new BigDecimal("90000.00"))
                .creditScore(750)
                .employmentStatus("SELF_EMPLOYED")
                .existingDebt(new BigDecimal("20000.00"))
                .build();

        customerRepository.saveAll(List.of(customer1, customer2, customer3));

        // Test finding by annual income greater than or equal to 70000
        List<Customer> highIncomeCustomers = customerRepository.findByAnnualIncomeGreaterThanEqual(new BigDecimal("70000.00"));
        assertThat(highIncomeCustomers).hasSize(2);
        assertThat(highIncomeCustomers).extracting(Customer::getName)
                .containsExactlyInAnyOrder("Jane Smith", "Robert Johnson");
    }
}