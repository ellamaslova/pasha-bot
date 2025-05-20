package org.example;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    static RecordsDAO recordsDAO;

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

        recordsDAO = new RecordsDAO(jdbcClient);

        menu();
    }

    private static void menu() {
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
                        recordsDAO.insert(dailyRecord);
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

    private static DailyRecord readRecord(Scanner scanner)  {
        LocalDate currentDay = LocalDate.now();
        DailyRecord recordAddedToday = recordsDAO.getRecordByDate(currentDay);
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

    private static void generateReport() {
        System.out.println(recordsDAO.getStatistics());
    }

    private static void searchBySymptom(Scanner scanner) {
        Statistics statistics = recordsDAO.getStatistics();
        while (true) {
            System.out.printf("""
                    1. насморк (%d записей)
                    2. температура (%d записей)
                    3. прогул (%d записей)
                    4. выход
                    %n""", statistics.getDaysWithSnot(), statistics.getDaysWithTemperature(), statistics.getDaysNoShow());
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 1 до 4");
                continue;
            }
            switch (choice) {
                case 1:
                    recordsDAO.getSnotRecords().forEach(System.out::println);
                    break;
                case 2:
                    recordsDAO.getTemperatureRecords().forEach(System.out::println);
                    break;
                case 3:
                    recordsDAO.getNoShowRecords().forEach(System.out::println);
                    break;
                case 4:
                    System.out.println("Выход из программы...");
                    return;
                default:
                    System.out.println("Ошибка: введите число от 1 до 4");
            }
        }
    }
}