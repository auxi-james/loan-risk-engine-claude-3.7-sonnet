package com.loanrisk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Database configuration for different profiles
 */
@Configuration
public class DatabaseConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    
    @Autowired
    private DataSource dataSource;
    
    /**
     * Development profile configuration
     */
    @Profile("dev")
    @Bean
    public CommandLineRunner devDatabaseSetup() {
        return args -> {
            logger.info("Development database initialized with H2");
            logger.info("H2 console available at /h2-console");
            logger.info("JDBC URL: {}", dataSource.getConnection().getMetaData().getURL());
        };
    }
    
    /**
     * Test profile configuration
     */
    @Profile("test")
    @Bean
    public CommandLineRunner testDatabaseSetup() {
        return args -> {
            logger.info("Test database initialized with H2");
            logger.info("JDBC URL: {}", dataSource.getConnection().getMetaData().getURL());
        };
    }
    
    /**
     * Production profile configuration
     */
    @Profile("prod")
    @Bean
    public CommandLineRunner prodDatabaseSetup() {
        return args -> {
            logger.info("Production database initialized with PostgreSQL");
            logger.info("JDBC URL: {}", dataSource.getConnection().getMetaData().getURL());
        };
    }
}