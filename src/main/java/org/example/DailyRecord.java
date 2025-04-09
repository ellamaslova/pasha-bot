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

//    public static DailyRecord fromCsv(String data) {
//        // TODO
//    }


}
