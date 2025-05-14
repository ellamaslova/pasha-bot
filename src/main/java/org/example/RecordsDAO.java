package org.example;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.time.LocalDate;

/**
 * DAO (database access object) это один из подходов,
 * когда логику взаимодействия с БД разделяют с остальной.
 * Такие классы делаются под отдельные таблицы
 * и отвечают только за формирование запросов к БД.
 */
@RequiredArgsConstructor
public class RecordsDAO {
    private final JdbcClient jdbcClient;

    DailyRecord getRecordByDate(LocalDate day) {
        return jdbcClient.sql("""
                SELECT kinder_garden_visit, snot, temperature, today
                FROM records
                WHERE today = ?
                """)
                .param(day)
                // данные из select автоматически сопоставляются
                // с именами полей класса DailyRecord
                // с учётом того, что в бд принят другой стиль именования колонок
                .query(DailyRecord.class)
                .single();
    }
}
