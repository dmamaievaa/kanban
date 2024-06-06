import exceptions.ManagerSaveException;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) throws ManagerSaveException {
        TaskManager taskManager = Managers.getDefault();
        checkMethods(taskManager);
        checkHistory(taskManager);

    }

    public static void printAllInstances(TaskManager taskManager) throws ManagerSaveException {
        System.out.println("Tasks:");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Subtasks");
        System.out.println(taskManager.getAllSubtasks());
        System.out.println("Epics");
        System.out.println(taskManager.getAllEpics());
    }

    public static void checkMethods(TaskManager taskManager) throws ManagerSaveException {
        Task task1 = new Task("First task", "First task description", Status.NEW, LocalDateTime.of(2025, 6, 4, 10, 0), Duration.ofHours(1));
        Task task2 = new Task("Second task", "Second task description", Status.IN_PROGRESS, LocalDateTime.of(2025, 6, 14, 10, 0), Duration.ofHours(1));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        Epic epic1 = new Epic("First epic", "First epic description");
        Epic epic2 = new Epic("Second epic", "Second epic description");
        Subtask subtask1InEpic1 = new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, 3,
                LocalDateTime.of(2024, 6, 4, 10, 0), Duration.ofHours(1));
        Subtask subtask2InEpic1 = new Subtask("Second subtask in epic 1",
                "Second subtask description", Status.IN_PROGRESS, 3,
                LocalDateTime.of(2024, 7, 4, 10, 0),
                Duration.ofHours(1));
        Subtask subtaskInEpic2 = new Subtask("Subtask in epic 2",
                "Subtask description", Status.NEW, 4,
                LocalDateTime.of(2024, 8, 4, 10, 0),
                Duration.ofHours(1));

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1InEpic1);
        taskManager.createSubtask(subtask2InEpic1);
        taskManager.createSubtask(subtaskInEpic2);
        printAllInstances(taskManager);

        Task task1Updated = new Task(1, "First task after update", "First task with new status",
                Status.IN_PROGRESS, LocalDateTime.of(2025, 6, 4, 10, 0), Duration.ofHours(1));
        taskManager.updateTask(task1Updated);
        Subtask subtaskInEpic2Updated = new Subtask(7, "Subtask in epic 4 after update",
                "Subtask was finished", Status.IN_PROGRESS, 4, LocalDateTime.of(2025, 9, 4, 10, 0), Duration.ofHours(1));
        taskManager.updateSubtask(subtaskInEpic2Updated);
        printAllInstances(taskManager);
        taskManager.removeTaskById(2);
        printAllInstances(taskManager);
    }

    public static void checkHistory(TaskManager taskManager) throws ManagerSaveException {
        Task task1 = new Task("First task", "First task description", Status.NEW, LocalDateTime.of(2025, 4, 4, 10, 0), Duration.ofHours(1));
        taskManager.createTask(task1);
        Task task2 = new Task("Second task", "Second task description", Status.IN_PROGRESS, LocalDateTime.of(2025, 6, 30, 10, 0), Duration.ofHours(1));
        taskManager.createTask(task2);
        Epic epic = new Epic("Epic title", "Epic description");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic.getId(),
                LocalDateTime.of(2024, 10, 4, 10, 0), Duration.ofHours(1));
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask title",
                "Subtask description", Status.NEW, epic.getId(),
                LocalDateTime.of(2024, 11, 4, 10, 0), Duration.ofHours(1));
        taskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask title",
                "Subtask description", Status.NEW, epic.getId(),
                LocalDateTime.of(2024, 1, 4, 10, 0), Duration.ofHours(1));
        taskManager.createSubtask(subtask3);
        taskManager.getEpicById(epic.getId());
        System.out.println("View history: ");
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(task2.getId());
        System.out.println("View history: ");
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(task1.getId());
        System.out.println("View history: ");
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(subtask2.getId());
        System.out.println("View history: ");
        System.out.println(taskManager.getHistory());
        taskManager.removeEpicById(epic.getId());
        System.out.println(taskManager.getHistory());
        System.out.println("Prioritized by start time tasks:");
        System.out.println(taskManager.getPrioritizedTasks());
    }
}
