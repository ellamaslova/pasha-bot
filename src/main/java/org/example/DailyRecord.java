package org.example;

import java.time.LocalDate;

public record DailyRecord(
        boolean kinderGardenVisit,
        boolean snot,
        boolean temperature,
        LocalDate today
        //Set<Mood> moods
) {
}
