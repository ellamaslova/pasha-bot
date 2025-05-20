package org.example;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsTest {

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
