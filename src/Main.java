import task.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Scanner;

public class Main {
    private static final Schedule SCHEDULE = new Schedule();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SCHEDULE.addTask(new SingleTask("SingleTest", "Testiruem", LocalDateTime.now(), TaskType.PERSONAL));
        SCHEDULE.addTask(new DailyTask("DailyTest", "Testiruem", LocalDateTime.now(), TaskType.WORK));
        SCHEDULE.addTask(new MonthlyTask("MonthlyTest", "Testiruem", LocalDateTime.now(), TaskType.WORK));
        SCHEDULE.addTask(new WeeklyTask("WeeklyTest", "Testiruem", LocalDateTime.now().plusHours(1), TaskType.PERSONAL));
        SCHEDULE.addTask(new YearlyTask("YearlyTest", "Testiruem", LocalDateTime.now().plusHours(3), TaskType.PERSONAL));

        printTasksForDate(scanner);

    }

    private static void printTasksForDate(Scanner scanner) {
        LocalDate localDate = readDate(scanner);
        Collection<Task> getTaskForDate = SCHEDULE.getTasksToDate(localDate); // Получаем коллекцию задач передавая в расписание дату
        System.out.println("Задачи на " + localDate.format(DATE_FORMAT));
        for (Task task : getTaskForDate) {
            System.out.printf("[%s] %s: %s (%s)%n",
                    localizeType(task.getTaskType()),
                    task.getTitle(),
                    task.getTaskDateTime().format(TIME_FORMAT),
                    task.getDescription());
        }
    }

    private static LocalDate readDate(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите дату задачи в формате dd.MM.yyyy: ");
                String dateLine = scanner.nextLine();
                return LocalDate.parse(dateLine, DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Некорректный формат даты");
            }
        }
    }


    private static String localizeType(TaskType taskType) {
        return switch (taskType) {
            case WORK -> "Рабочая задача";
            case PERSONAL -> "Персональная задача";
            default -> "Неизвестный тип задачи";
        };
    }
}