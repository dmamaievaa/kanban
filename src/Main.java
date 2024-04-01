import Manager.TaskManager;
import Task.Task;
import Task.Epic;
import Task.Subtask;
import Task.Status;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task(1, "First task", "First task description", Status.NEW);
        Task task2 = new Task(2, "Second task", "Second task description", Status.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        Epic epic1 = new Epic(1, "First epic", "First epic description");
        Epic epic2 = new Epic(2, "Second epic", "Second epic description");
        Subtask subtask1InEpic1 = new Subtask(1, "First subtask in epic 1",
                "First subtask description", Status.NEW, 1);
        Subtask subtask2InEpic1 = new Subtask(2, "Second subtask in epic 1",
                "Second subtask description", Status.IN_PROGRESS, 1);
        Subtask subtaskInEpic2 = new Subtask(3, "Subtask in epic 2",
                "Subtask description", Status.IN_PROGRESS, 2);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1InEpic1);
        taskManager.createSubtask(subtask2InEpic1);
        taskManager.createSubtask(subtaskInEpic2);
        taskManager.printAllInstances();
        Task task1Updated = new Task(1, "First task after update", "First task with new status",
                Status.NEW);
        taskManager.updateTask(task1Updated);
        Subtask subtaskInEpic2Updated = new Subtask(3, "Subtask in epic 2 after update",
                "Subtask was finished", Status.IN_PROGRESS, 2);
        taskManager.updateSubtask(subtaskInEpic2Updated);
        taskManager.printAllInstances();
        taskManager.removeTaskById(1);
        taskManager.removeEpicById(1);
        taskManager.printAllInstances();

    }
}
