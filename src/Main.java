import TaskNotFoundException.TaskNotFoundException;
import task.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Scanner;

public class Main {
    private static final Schedule SCHEDULE = new Schedule();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SCHEDULE.addTask(new SingleTask("SingleTest", "Testiruem", LocalDateTime.now().plusHours(1), TaskType.PERSONAL));
        SCHEDULE.addTask(new DailyTask("DailyTest", "Testiruem", LocalDateTime.now().plusHours(5), TaskType.WORK));
        SCHEDULE.addTask(new MonthlyTask("MonthlyTest", "Testiruem", LocalDateTime.now().plusHours(6), TaskType.WORK));
        SCHEDULE.addTask(new WeeklyTask("WeeklyTest", "Testiruem", LocalDateTime.now().plusHours(8), TaskType.PERSONAL));
        SCHEDULE.addTask(new YearlyTask("YearlyTest", "Testiruem", LocalDateTime.now().plusHours(10), TaskType.PERSONAL));

//        addTask(scanner);
//        System.out.println();
//        removeTasks(scanner);
//        System.out.println();
//        printTasksForDate(scanner);
//        System.out.println();
        startWork(scanner);

    }

    public static void addTask(Scanner scanner) {
        String title = readString("Введите наименование задачи: ", scanner);
        String description = readString("Введите описание задачи: ", scanner);
        LocalDateTime taskDate = readDateTime(scanner);
        TaskType taskType = readType(scanner);
        Repeatability repeatability = readRepeatability(scanner);
        Task task = switch (repeatability) {

            case SINGLE -> new SingleTask(title,description,taskDate,taskType);
            case DAILY -> new DailyTask(title,description,taskDate,taskType);
            case WEEKLY -> new WeeklyTask(title,description,taskDate,taskType);
            case MONTHLY -> new MonthlyTask(title,description,taskDate,taskType);
            case YEARLY -> new YearlyTask(title,description,taskDate,taskType);
        };
        SCHEDULE.addTask(task);
        System.out.println("Задача [" + task.getTitle() + "] добавлена в расписание");
        System.out.println();
    }

    private static Repeatability readRepeatability(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Выберите тип повторяемости задачи: ");
                for (Repeatability  repeatability : Repeatability.values()) {
                    System.out.println(repeatability.ordinal() + ". " + localizeRepeatability(repeatability));
                }
                System.out.print("Введите тип: ");
                String ordinalLine = scanner.nextLine();
                int ordinal = Integer.parseInt(ordinalLine);
                return Repeatability.values()[ordinal];
            } catch (NumberFormatException e) {
                System.out.println("Введен неверный номер типа повторяемости задачи");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Тип повторяемости задачи не найден");
            }
        }
    }

    private static TaskType readType(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Выберите тип задачи: ");
                for (TaskType taskType : TaskType.values()) {
                    System.out.println(taskType.ordinal() + ". " + localizeType(taskType));
                }
                System.out.print("Введите тип: ");
                String ordinalLine = scanner.nextLine();
                int ordinal = Integer.parseInt(ordinalLine);
                return TaskType.values()[ordinal];
            } catch (NumberFormatException e) {
                System.out.println("Введен неверный номер типа задачи");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Тип задачи не найден");
            }
        }
    }

    private static LocalDateTime readDateTime(Scanner scanner) {
        LocalDate localDate = readDate(scanner);
        LocalTime localTime= readTime(scanner);
        return localDate.atTime(localTime);

    }

    private static String readString(String message, Scanner scanner) {
        while (true) {
            System.out.println(message);
            String readString = scanner.nextLine();
            if (readString == null || readString.isBlank()) {
                System.out.println("Введено пустое значение");
            } else {
                return readString;
            }
        }
    }
    
    

    private static void removeTasks(Scanner scanner) {
        System.out.println("Все задачи:");
        for (Task task : SCHEDULE.getAllTasks()) {
            System.out.printf("%d. %s [%s](%s)%n",
                    task.getId(),
                    task.getTitle(),
                    localizeType(task.getTaskType()),
                    localizeRepeatability(task.getRepeatabilitiType()));
        }
        while (true) {
            try {
                System.out.println("Выберите задачу для удаления");
                String idLine = scanner.nextLine();
                int id = Integer.parseInt(idLine);
                SCHEDULE.removeTask(id);
                System.out.println("Задача удалена");
                System.out.println();
                break;
            } catch (NumberFormatException e) {
                System.out.println("Введен неверный id задачи");
            } catch (TaskNotFoundException e) {
                System.out.println("Отсутсвует задача для удаления");
            }
        }
    }

    private static void printTasksForDate(Scanner scanner) {
        LocalDate localDate = readDate(scanner);
        Collection<Task> getTaskForDate = SCHEDULE.getTasksToDate(localDate); // Получаем коллекцию задач передавая в расписание дату
        System.out.println("Задачи на " + localDate.format(DATE_FORMAT) + ":");
        for (Task task : getTaskForDate) {
            System.out.printf("[%s] %s: %s (%s)%n",
                    localizeType(task.getTaskType()),
                    task.getTitle(),
                    task.getTaskDateTime().format(TIME_FORMAT),
                    task.getDescription());
        }
        System.out.println();
    }

    private static LocalDate readDate(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите дату в формате dd.MM.yyyy: ");
                String dateLine = scanner.nextLine();
                return LocalDate.parse(dateLine, DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Некорректный формат даты");
            }
        }
    }

    private static LocalTime readTime(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введите время задачи в формате hh:mm: ");
                String dateLine = scanner.nextLine();
                return LocalTime.parse(dateLine, TIME_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Некорректный формат");
            }
        }
    }

    public static void startWork(Scanner scanner) {
        int i = 0;
        while (i == 0) {
            try {
            System.out.println("Выберите интесеующий пукт: ");
            System.out.println("1. Добавить задачу");
            System.out.println("2. Получить задачи на день");
            System.out.println("3. Удалить задачу");
            System.out.println("4. Выход из программы");
            String idChoice = scanner.nextLine();
            int id = Integer.parseInt(idChoice);
            switch (id) {
                case 1:
                    System.out.println("Выбран пункт [Добавить задачу]");
                    addTask(scanner);
                    break;
                case 2:
                    System.out.println("Выбран пункт [Получить задачи на день]");
                    printTasksForDate(scanner);
                    break;
                case 3:
                    System.out.println("Выбран пункт [Удалить задачу]");
                    removeTasks(scanner);
                    break;
                case 4:
                    System.out.println("Выбран пункт [Выйти из программы]. Досвидания!");
                    i += 1;
                    break;
                default:
                    System.out.println("Введен неверный id задачи, поробуйте еще раз!");
            }
            } catch (NumberFormatException e) {
                System.out.println("Введен неверный id задачи, попробуйте еще раз!");
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

    private static String localizeRepeatability(Repeatability repeatability) {
        return switch (repeatability) {
            case SINGLE -> "Разовая";
            case DAILY -> "Ежедневная";
            case WEEKLY -> "Еженедельная";
            case MONTHLY -> "Ежемесячная";
            case YEARLY -> "Ежегодная";
        };
    }
}