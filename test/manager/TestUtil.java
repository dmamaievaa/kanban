package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class TestUtil {
    public static Task createFirstTask() {
        return new Task("Task1", "Task1 description", Status.NEW,
                LocalDateTime.now(), Duration.ofHours(1));
    }

    public static Task createSecondTask() {
        return new Task("Task1", "Task1 description", Status.NEW,
                LocalDateTime.of(2024, 6, 4, 10, 0),
                Duration.ofHours(1));
    }

    public static Epic createFirstEpic() {
        return new Epic(1, "Epic", "Epic description");
    }
    public static Epic createSecondEpic() {
        return new Epic(6, "Epic", "Epic description");
    }

    public static Subtask createFirstSubtask(Epic epic) {
        return new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic.getId(),
                LocalDateTime.of(2024, 6, 7, 10, 0),
                Duration.ofHours(1));
    }

    public static Subtask createSecondSubtask(Epic epic) {
        return new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic.getId(),
                LocalDateTime.of(2024, 6, 5, 10, 0),
                Duration.ofHours(2));
    }
}
