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
        menu();
    }

    private static void generateReport() throws IOException {
        List<DailyRecord> records = Files.readAllLines(FILE_PATH).stream()
                .map(DailyRecord::fromCsv)
                .toList();
        System.out.println(Statistics.createStatistics(records));
    }

    private static DailyRecord readRecord(Scanner scanner) {
        System.out.println("Паша пошел сегодня в сад? (Да / Нет, Yes / No)");
        boolean kinderGardenVisit = checkAnswer(scanner);
        System.out.println("Есть ли сопли? (Да / Нет, Yes / No)");
        boolean snot = checkAnswer(scanner);

        System.out.println("Температура повышена? (Да / Нет, Yes / No)");
        boolean temperature = checkAnswer(scanner);

        LocalDate today = LocalDate.now();

        return new DailyRecord(kinderGardenVisit, snot, temperature, today);
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

    private static void menu() throws IOException {
        while (true) {
            System.out.println("=== Меню трекера здоровья ===");
            System.out.println("1. Записать данные за день");
            System.out.println("2. Посмотреть статистику");
            System.out.println("3. Найти по симптому");
            System.out.println("4. Выход");
            System.out.print("Выберите действие (1-4): ");

            int choice;
            Scanner scanner;
            try {
                scanner = new Scanner(System.in);
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 1 до 4");
                continue;
            }
            switch (choice) {
                case 1:
                    readRecord(scanner);
                    var dailyRecord = readRecord(scanner);
                    Files.writeString(FILE_PATH, dailyRecord.toCsv() + "\n", StandardOpenOption.APPEND);
                    break;
                case 2:
                    generateReport();
                    break;
                case 3:
                    searchBySymptom();
                    break;
                case 4:
                    System.out.println("Выход из программы...");
                    return;
                default:
                    System.out.println("Неверный ввод. Попробуйте снова.");
            }
        }
    }

    private static void searchBySymptom() {
        // TODO...
    }
}