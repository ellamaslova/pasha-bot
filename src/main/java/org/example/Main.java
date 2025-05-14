package org.example;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    // TODO: от этого надо отказаться
    private static final Path FILE_PATH = Path.of("records.csv");

    public static void main(String[] args) throws IOException {
        // читаем конфигурацию
        Config config = Config.readConfig("config.properties");

        // подключаемся к бд
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(config.getDbDriver());
        dataSource.setUrl(config.getDbJdbcUrl());
        dataSource.setUsername(config.getDbUser());
        dataSource.setPassword(config.getDbPassword());
        JdbcClient jdbcClient = JdbcClient.create(dataSource);

        RecordsDAO recordsDAO = new RecordsDAO(jdbcClient);  // TODO: надо бы это задействовать вместо файла

        menu();
    }

    private static void menu() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("""
                === Меню трекера здоровья ===
                1. Записать данные за день
                2. Посмотреть статистику
                3. Найти по симптому
                4. Выход
                =============================
                Выберите действие (1-4):""");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 1 до 4");
                continue;
            }
            switch (choice) {
                case 1:
                    var dailyRecord = readRecord(scanner);
                    if (dailyRecord != null) {
                        Files.writeString(FILE_PATH, dailyRecord.toCsv() + "\n", StandardOpenOption.APPEND);
                    }
                    break;
                case 2:
                    generateReport();
                    break;
                case 3:
                    searchBySymptom(scanner);
                    break;
                case 4:
                    System.out.println("Выход из программы...");
                    return;
                default:
                    System.out.println("Ошибка: введите число от 1 до 4");
            }
        }
    }

    private static DailyRecord readRecord(Scanner scanner) throws IOException {
        LocalDate currentDay = LocalDate.now();
        DailyRecord recordAddedToday = getTodayRecord(currentDay);
        if (recordAddedToday != null) {
            System.out.println("Запись за сегодня уже существует");
            System.out.println(recordAddedToday);
            return null;
        }
        System.out.println("Паша пошел сегодня в сад? (Да / Нет, Yes / No)");
        boolean kinderGardenVisit = checkAnswer(scanner);

        System.out.println("Есть ли сопли? (Да / Нет, Yes / No)");
        boolean snot = checkAnswer(scanner);

        System.out.println("Температура повышена? (Да / Нет, Yes / No)");
        boolean temperature = checkAnswer(scanner);

        return new DailyRecord(kinderGardenVisit, snot, temperature, LocalDate.now());
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

    private static void generateReport() throws IOException {
        List<DailyRecord> records = Files.readAllLines(FILE_PATH).stream()
                .map(DailyRecord::fromCsv)
                .toList();
        System.out.println(Statistics.createStatistics(records));
    }

    private static void searchBySymptom(Scanner scanner) throws IOException {
        List<DailyRecord> records = Files.readAllLines(FILE_PATH).stream()
                .map(DailyRecord::fromCsv)
                .toList();
        int daysWithSnot = 0;
        int daysWithTemperature = 0;
        int daysNoShow = 0;
        List<DailyRecord> snotRecords = new ArrayList<>();
        List<DailyRecord> temperatureRecords = new ArrayList<>();
        List<DailyRecord> noShowRecords = new ArrayList<>();
        for (DailyRecord record : records) {
            if (record.snot()) {
                daysWithSnot++;
                snotRecords.add(record);
            }
            if (record.temperature()) {
                daysWithTemperature++;
                temperatureRecords.add(record);
            }
            if (!record.kinderGardenVisit()) {
                daysNoShow++;
                noShowRecords.add(record);
            }
        }
        while (true) {
            System.out.printf("""
                    1. насморк (%d записей)
                    2. температура (%d записей)
                    3. прогул (%d записей)
                    4. выход
                    %n""", daysWithSnot, daysWithTemperature, daysNoShow);
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 1 до 4");
                continue;
            }
            switch (choice) {
                case 1:
                    snotRecords.forEach(System.out::println);
                    break;
                case 2:
                    temperatureRecords.forEach(System.out::println);
                    break;
                case 3:
                    noShowRecords.forEach(System.out::println);
                    break;
                case 4:
                    System.out.println("Выход из программы...");
                    return;
                default:
                    System.out.println("Ошибка: введите число от 1 до 4");
            }
        }
    }

    private static DailyRecord getTodayRecord(LocalDate today) throws IOException {
        if (!Files.exists(FILE_PATH))
            return null;

        List<DailyRecord> lines = Files.readAllLines(FILE_PATH).stream()
                .map(DailyRecord::fromCsv)
                .toList();
        DailyRecord lastRecord = lines.getLast();
        return lastRecord.today().equals(today) ? lastRecord : null;
    }
}