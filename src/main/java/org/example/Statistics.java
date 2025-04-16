package org.example;

import java.time.LocalDate;
import java.util.List;

public class Statistics {
    private int daysWithSnot;
    private int daysWithTemperature;
    private int numberOfKinderGardenVisit;
    private float percentageOfNoShows;
    private float percentageOfTemperature;
    private float percentageOfSnot;
    private LocalDate lastDayWithSnot;
    private LocalDate lastDayWithTemperature;
    private LocalDate lastVisit;


    public static Statistics createStatistics(List<DailyRecord> records) {
        if (records.isEmpty()) {
            System.out.println("Нет данных для отчета");
            return null;
        }
        Statistics stat = new Statistics();
        for (DailyRecord record : records) {
            if (record.kinderGardenVisit()) {
                stat.numberOfKinderGardenVisit++;
                stat.lastVisit = record.today();
            }
            if (record.snot()) {
                stat.daysWithSnot++;
                stat.lastDayWithSnot = record.today();
            }
            if (record.temperature()) {
                stat.daysWithTemperature++;
                stat.lastDayWithTemperature = record.today();
            }
            stat.percentageOfNoShows = (float) Math.round((float) stat.numberOfKinderGardenVisit / records.size() * 100 * 100) / 100;
            stat.percentageOfSnot = (float) Math.round((float) stat.daysWithSnot / records.size() * 100 * 100) / 100;
            stat.percentageOfTemperature = (float) Math.round((float) stat.daysWithTemperature / records.size() * 100 * 100) / 100;
        }

        return stat;
    }

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
