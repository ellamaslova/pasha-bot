package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Statistics {
    private int daysWithSnot;
    private int daysWithTemperature;
    private int numberOfKinderGardenVisit;
    private int daysNoShow;
    private float percentageOfNoShows;
    private float percentageOfTemperature;
    private float percentageOfSnot;
    private LocalDate lastDayWithSnot;
    private LocalDate lastDayWithTemperature;
    private LocalDate lastVisit;

    @Override
    public String toString() {
        return "=== Статистический отчет ===\n"
            + "Процент прогула: " + percentageOfNoShows + "%\n"
            + "Процент дней с насморком: " + percentageOfSnot + "%\n"
            + "Процент дней с температурой: " + percentageOfTemperature + "%\n"
            + "Количество дней с насморком: " + daysWithSnot + "\n"
            + "Количество дней с температурой: " + daysWithTemperature + "\n"
            + "Количество посещений: " + numberOfKinderGardenVisit + "\n"
            + "Последнее посещение: " + lastVisit + "\n"
            + "В последний день температура была: " + lastDayWithTemperature + "\n"
            + "В последний день насморк был: " + lastDayWithSnot;
    }

}
