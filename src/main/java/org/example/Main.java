package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Path FILE_PATH = Path.of("records.csv");

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        var dailyRecord = readRecord(scanner);
        System.out.println(dailyRecord);
        assert dailyRecord != null;
        Files.writeString(FILE_PATH, dailyRecord.toCsv() + "\n", StandardOpenOption.APPEND);
        generateReport();
    }

    private static void generateReport() throws IOException {
        List<DailyRecord> records = Files.readAllLines(FILE_PATH).stream()
                .map(DailyRecord::fromCsv)
                .toList();
        System.out.println(Statistics.createStatistics(records));
    }

    private static DailyRecord readRecord(Scanner scanner) throws IOException {
        LocalDate currentDay = LocalDate.now();
        DailyRecord recordAddedToday = getRecordAddedToday(currentDay);
        if (recordAddedToday != null) {
            System.out.println("Запись за сегодня уже существует");
            System.out.println(recordAddedToday);
        }
        else {

            System.out.println("Паша пошел сегодня в сад? (Да / Нет, Yes / No)");
            boolean kinderGardenVisit = checkAnswer(scanner);

            System.out.println("Есть ли сопли? (Да / Нет, Yes / No)");
            boolean snot = checkAnswer(scanner);

            System.out.println("Температура повышена? (Да / Нет, Yes / No)");
            boolean temperature = checkAnswer(scanner);

            LocalDate today = LocalDate.now();

            return new DailyRecord(kinderGardenVisit, snot, temperature, today);
        }
        return null;
    }

    private static boolean checkAnswer(Scanner scanner) {
        String answer = scanner.nextLine();
        while (!answer.equalsIgnoreCase("Да") && !answer.equalsIgnoreCase("Нет")
                && !answer.equalsIgnoreCase("Д") && !answer.equalsIgnoreCase("Н")
                && !answer.equalsIgnoreCase("Yes") && !answer.equalsIgnoreCase("No")
                && !answer.equalsIgnoreCase("Y") && !answer.equalsIgnoreCase("N")) {
            System.out.println("Выбери вариант из предложенного");
            answer = scanner.nextLine();
        }
        return answer.equalsIgnoreCase("Да") || answer.equalsIgnoreCase("Д")
                || answer.equalsIgnoreCase("Yes") || answer.equalsIgnoreCase("Y");
    }

    private static DailyRecord getRecordAddedToday(LocalDate today) throws IOException {
        if (!Files.exists(FILE_PATH))
            return null;

        List<DailyRecord> lines = Files.readAllLines(FILE_PATH).stream()
                .map(DailyRecord::fromCsv)
                .toList();
        DailyRecord lastRecord = lines.getLast();
        return lastRecord.today().equals(today) ? lastRecord : null;
    }
}