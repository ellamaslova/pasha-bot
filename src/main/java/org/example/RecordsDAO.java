package org.example;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.time.LocalDate;
import java.util.List;

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
                .optional().orElse(null);
    }

    void insert(DailyRecord dailyRecord) {
        jdbcClient.sql("""
            insert into records (kinder_garden_visit, snot, temperature, today)
            values (?, ?, ?, ?)
            """)
                .params(dailyRecord.kinderGardenVisit(),
                        dailyRecord.snot(),
                        dailyRecord.temperature(),
                        dailyRecord.today())
                .update();
    }

    Statistics getStatistics() {
        return jdbcClient.sql("""
        SELECT
            COUNT(*) FILTER (WHERE snot) AS daysWithSnot,
            COUNT(*) FILTER (WHERE temperature) AS daysWithTemperature,
            COUNT(*) FILTER (WHERE kinder_garden_visit) AS numberOfKinderGardenVisit,
            COUNT(*) FILTER (WHERE NOT kinder_garden_visit) AS daysNoShow,
            ROUND(COUNT(*) FILTER (WHERE NOT kinder_garden_visit) * 100.0 / COUNT(*), 2) AS percentageOfNoShows,
            ROUND(COUNT(*) FILTER (WHERE temperature) * 100.0 / COUNT(*), 2) AS percentageOfTemperature,
            ROUND(COUNT(*) FILTER (WHERE snot) * 100.0 / COUNT(*), 2) AS percentageOfSnot,
            MAX(today) FILTER (WHERE snot) AS lastDayWithSnot,
            MAX(today) FILTER (WHERE temperature) AS lastDayWithTemperature,
            MAX(today) FILTER (WHERE kinder_garden_visit) AS lastVisit
        FROM records
        """)
                .query(Statistics.class)
                .single();
    }

    List<DailyRecord> getSnotRecords() {
        return jdbcClient.sql("""
            SELECT * FROM records
            WHERE snot
            """)
                .query(DailyRecord.class)
                .list();
    }

    List<DailyRecord> getTemperatureRecords() {
        return jdbcClient.sql("""
            SELECT * FROM records
            WHERE temperature
            """)
                .query(DailyRecord.class)
                .list();
    }

    List<DailyRecord> getNoShowRecords() {
        return jdbcClient.sql("""
            SELECT * FROM records
            WHERE kinder_garden_visit = FALSE
            """)
                .query(DailyRecord.class)
                .list();
    }
}
