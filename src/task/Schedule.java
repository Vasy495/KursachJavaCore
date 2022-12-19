package task;

import java.time.LocalDate;
import java.util.*;

public class Schedule {
    private final Map<Integer, Task> tasks = new HashMap<>();

    public void addTask(Task task) {
        this.tasks.put(task.getId(), task);
    }

    public void removeTask(int id) {
        this.tasks.remove(id);
    }

    public Collection<Task> getTasksToDate(LocalDate date) {
        List<Task> tasksForDate = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.appearsIn(date)) {
                tasksForDate.add(task);
            }
        }
        return tasksForDate;
    }
}


