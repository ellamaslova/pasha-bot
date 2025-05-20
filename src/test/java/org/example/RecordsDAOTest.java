package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
class RecordsDAOTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine")
            .withInitScript("init.sql");

    static JdbcClient jdbcClient;
    static RecordsDAO recordsDAO;

    @BeforeAll
    static void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(postgres.getDriverClassName());
        dataSource.setUrl(postgres.getJdbcUrl());
        dataSource.setUsername(postgres.getUsername());
        dataSource.setPassword(postgres.getPassword());
        jdbcClient = JdbcClient.create(dataSource);

        recordsDAO = new RecordsDAO(jdbcClient);
    }

    @Test
    void getRecordByDate() {
        LocalDate day = LocalDate.of(2025, 1, 1);
        DailyRecord recordByDate = recordsDAO.getRecordByDate(day);
        assertNotNull(recordByDate);
        assertEquals(day, recordByDate.today());
    }

    @Test
    void getStatistics() {
        Statistics statistics = recordsDAO.getStatistics();
        assertEquals(6, statistics.getDaysWithSnot());
        assertEquals(1, statistics.getDaysWithTemperature());
        assertEquals(7, statistics.getNumberOfKinderGardenVisit());
        assertEquals(3, statistics.getDaysNoShow());
        assertEquals(30, statistics.getPercentageOfNoShows());
        assertEquals(10, statistics.getPercentageOfTemperature());
        assertEquals(60, statistics.getPercentageOfSnot());
        assertEquals(LocalDate.of(2025, 4, 18), statistics.getLastDayWithSnot());
        assertEquals(LocalDate.of(2025, 1, 3), statistics.getLastDayWithTemperature());
        assertEquals(LocalDate.of(2025, 4, 22), statistics.getLastVisit());
    }
}