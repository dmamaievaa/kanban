import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import task.Task;
import task.Epic;
import task.Subtask;
import task.Status;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        checkMethods(inMemoryTaskManager);
        checkHistory(inMemoryTaskManager);
    }

    public static void checkMethods(InMemoryTaskManager inMemoryTaskManager) {
        Task task1 = new Task("First task", "First task description", Status.NEW);
        Task task2 = new Task("Second task", "Second task description", Status.IN_PROGRESS);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        Epic epic1 = new Epic("First epic", "First epic description");
        Epic epic2 = new Epic("Second epic", "Second epic description");
        Subtask subtask1InEpic1 = new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, 3);
        Subtask subtask2InEpic1 = new Subtask("Second subtask in epic 1",
                "Second subtask description", Status.IN_PROGRESS, 3);
        Subtask subtaskInEpic2 = new Subtask("Subtask in epic 2",
                "Subtask description", Status.NEW, 4);

        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createEpic(epic2);
        inMemoryTaskManager.createSubtask(subtask1InEpic1);
        inMemoryTaskManager.createSubtask(subtask2InEpic1);
        inMemoryTaskManager.createSubtask(subtaskInEpic2);
        inMemoryTaskManager.printAllInstances();

        Task task1Updated = new Task(1, "First task after update", "First task with new status",
                Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(task1Updated);
        Subtask subtaskInEpic2Updated = new Subtask(7, "Subtask in epic 4 after update",
                "Subtask was finished", Status.IN_PROGRESS, 4);
        inMemoryTaskManager.updateSubtask(subtaskInEpic2Updated);
        inMemoryTaskManager.printAllInstances();
        inMemoryTaskManager.removeTaskById(2);
        // inMemoryTaskManager.removeEpicById(3);
        inMemoryTaskManager.printAllInstances();
    }

    public static void checkHistory(InMemoryTaskManager inMemoryTaskManager) {
        TaskManager taskManager = Managers.getDefault();
        Task[] tasks = new Task[9];
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = new Task("Task title", "Task description", Status.NEW);
        }
        for (Task task : tasks) {
            inMemoryTaskManager.createTask(task);
            inMemoryTaskManager.getTaskById(task.getId());
        }
        Epic epicHistory = new Epic("Epic title", "Epic description");
        inMemoryTaskManager.createEpic(epicHistory);
        inMemoryTaskManager.getEpicById(epicHistory.getId());
        Subtask subtaskHistory = new Subtask("Subtask title",
                "Subtask description", Status.NEW, epicHistory.getId());
        inMemoryTaskManager.createSubtask(subtaskHistory);
        inMemoryTaskManager.getSubtaskById(subtaskHistory.getId());
        System.out.println("View history: ");
        System.out.println(inMemoryTaskManager.getHistory());
    }
}
