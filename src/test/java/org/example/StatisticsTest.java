package org.example;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsTest {

    @Test
    void createStatistics_withEmptyList_returnsNull() {
        List<DailyRecord> records = List.of();
        Statistics result = Statistics.createStatistics(records);
        assertNull(result);
    }

    @Test
    void createStatistics_withMixedRecords_calculatesCorrectly() {
        // Подготовка тестовых данных
        List<DailyRecord> records = List.of(
                new DailyRecord(true, false, false, LocalDate.of(2023, 12, 1)), // посещение
                new DailyRecord(false, true, false, LocalDate.of(2023, 12, 2)), // насморк
                new DailyRecord(false, false, true, LocalDate.of(2023, 12, 3)), // температура
                new DailyRecord(true, true, false, LocalDate.of(2023, 12, 4))  // посещение + насморк
        );

        Statistics result = Statistics.createStatistics(records);

        // Проверки
        assertNotNull(result);
        assertEquals(2, result.getNumberOfKinderGardenVisit());
        assertEquals(2, result.getDaysWithSnot());
        assertEquals(1, result.getDaysWithTemperature());

        assertEquals(LocalDate.of(2023, 12, 4), result.getLastVisit());
        assertEquals(LocalDate.of(2023, 12, 4), result.getLastDayWithSnot());
        assertEquals(LocalDate.of(2023, 12, 3), result.getLastDayWithTemperature());

        assertEquals(50.0f, result.getPercentageOfNoShows());  // 2/4 = 50%
        assertEquals(50.0f, result.getPercentageOfSnot());     // 2/4 = 50%
        assertEquals(25.0f, result.getPercentageOfTemperature()); // 1/4 = 25%
    }

    @Test
    void createStatistics_withAllVisits_calculates100Percent() {
        List<DailyRecord> records = List.of(
                new DailyRecord(true, false, false, LocalDate.now()),
                new DailyRecord(true, false, false, LocalDate.now().minusDays(1))
        );

        Statistics result = Statistics.createStatistics(records);

        assertNotNull(result);
        assertEquals(0.0f, result.getPercentageOfNoShows());
        assertEquals(0.0f, result.getPercentageOfSnot());
        assertEquals(0.0f, result.getPercentageOfTemperature());
    }

    @Test
    void testToString() {
        Statistics stats = new Statistics(
                5,                          // daysWithSnot
                3,                          // daysWithTemperature
                10,                         // numberOfKinderGardenVisit
                3,                          // daysNoShow
                20.0f,                      // percentageOfNoShows
                30.0f,                      // percentageOfTemperature
                50.0f,                      // percentageOfSnot
                LocalDate.of(2023, 12, 1),   // lastDayWithSnot
                LocalDate.of(2023, 12, 5),   // lastDayWithTemperature
                LocalDate.of(2025, 4, 20)  // lastVisit
        );
        assertEquals("""
                === Статистический отчет ===
                Процент прогула: 20.0%
                Процент дней с насморком: 50.0%
                Процент дней с температурой: 30.0%
                Количество дней с насморком: 5
                Количество дней с температурой: 3
                Количество посещений: 10
                Последнее посещение: 2025-04-20
                В последний день температура была: 2023-12-05
                В последний день насморк был: 2023-12-01""", stats.toString());
    }
}
