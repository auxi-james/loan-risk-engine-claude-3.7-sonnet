package com.loanrisk.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class DatabaseConfigTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testDatabaseConnection() throws SQLException {
        assertNotNull(dataSource, "DataSource should not be null");
        assertNotNull(dataSource.getConnection(), "Connection should not be null");
        assertTrue(dataSource.getConnection().getMetaData().getURL().contains("h2"), 
                "Test database should be H2");
    }
}