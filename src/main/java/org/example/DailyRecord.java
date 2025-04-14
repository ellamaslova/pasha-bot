package org.example;

import java.time.LocalDate;

public record DailyRecord(
        boolean kinderGardenVisit,
        boolean snot,
        boolean temperature,
        LocalDate today
        //Set<Mood> moods
) {

    public String toCsv() {
        return (kinderGardenVisit ? 1 : 0) + ";" +
                (snot ? 1 : 0) + ";" +
                (temperature ? 1 : 0) + ";" +
                today;
    }


    public static DailyRecord fromCsv(String recordLine) {
        String[] parts = recordLine.split(";");
        boolean kinderGardenVisit = "1".equals(parts[0]);
        boolean snot = "1".equals(parts[1]);
        boolean temperature = "1".equals(parts[2]);
        LocalDate today = LocalDate.parse(parts[3]);
        return new DailyRecord(kinderGardenVisit,
                snot, temperature, today);
    }
}
