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
        DailyRecord dailyRecord = readRecord(scanner);
        System.out.println(dailyRecord);
        Files.writeString(FILE_PATH, dailyRecord.toCsv() + "\n", StandardOpenOption.APPEND);
    }

//    private static String generateReport() throws IOException {
//        List<DailyRecord> records = Files.readAllLines(FILE_PATH).stream()
//                .map(DailyRecord::fromCsv)
//                .toList();
//
//
//    }

    private static DailyRecord readRecord(Scanner scanner) {
        System.out.println("Паша пошел сегодня в сад? (Да, Нет)");
        boolean kinderGardenVisit = checkAnswer(scanner);

        System.out.println("Есть ли сопли? (Да, Нет)");
        boolean snot = checkAnswer(scanner);

        System.out.println("Температура повышена? (Да, нет)");
        boolean temperature = checkAnswer(scanner);

        LocalDate today = LocalDate.now();

        return new DailyRecord(kinderGardenVisit, snot, temperature, today);
    }

    private static boolean checkAnswer(Scanner scanner) {
        String answer = scanner.nextLine();
        while (!answer.equalsIgnoreCase("Да") && !answer.equalsIgnoreCase("Нет")) {
            System.out.println("Выбери вариант из предложенного");
            answer = scanner.nextLine();
        }
        return answer.equalsIgnoreCase("Да");
    }
}