import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;


public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        checkMethods(taskManager);
        checkHistory(taskManager);

    }

    public static void printAllInstances(TaskManager taskManager) {
        System.out.println("Tasks:");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Subtasks");
        System.out.println(taskManager.getAllSubtasks());
        System.out.println("Epics");
        System.out.println(taskManager.getAllEpics());
    }

    public static void checkMethods(TaskManager taskManager) {
        Task task1 = new Task("First task", "First task description", Status.NEW);
        Task task2 = new Task("Second task", "Second task description", Status.IN_PROGRESS);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        Epic epic1 = new Epic("First epic", "First epic description");
        Epic epic2 = new Epic("Second epic", "Second epic description");
        Subtask subtask1InEpic1 = new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, 3);
        Subtask subtask2InEpic1 = new Subtask("Second subtask in epic 1",
                "Second subtask description", Status.IN_PROGRESS, 3);
        Subtask subtaskInEpic2 = new Subtask("Subtask in epic 2",
                "Subtask description", Status.NEW, 4);

        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1InEpic1);
        taskManager.createSubtask(subtask2InEpic1);
        taskManager.createSubtask(subtaskInEpic2);
        printAllInstances(taskManager);

        Task task1Updated = new Task(1, "First task after update", "First task with new status",
                Status.IN_PROGRESS);
        taskManager.updateTask(task1Updated);
        Subtask subtaskInEpic2Updated = new Subtask(7, "Subtask in epic 4 after update",
                "Subtask was finished", Status.IN_PROGRESS, 4);
        taskManager.updateSubtask(subtaskInEpic2Updated);
        printAllInstances(taskManager);
        taskManager.removeTaskById(2);
        printAllInstances(taskManager);
    }

    public static void checkHistory(TaskManager taskManager) {
        Task task1 = new Task("First task", "First task description", Status.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("Second task", "Second task description", Status.IN_PROGRESS);
        taskManager.createTask(task2);
        Epic epic = new Epic("Epic title", "Epic description");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask title",
                "Subtask description", Status.NEW, epic.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask title",
                "Subtask description", Status.NEW, epic.getId());
        taskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask title",
                "Subtask description", Status.NEW, epic.getId());
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
    }
}
