package manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;
import task.Epic;
import task.Subtask;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


// некоторые тесты не реализованы, тк они в принципе противоречат тз
public class InMemoryTaskManagerTest {
    protected TaskManager taskManager = Managers.getDefault();

    @BeforeEach
    void BeforeEach(){
        taskManager = Managers.getDefault();
    }
   //проверка, что InMemoryTaskManager добавляет задачи разного типа, может найти их по id и
   // история просмотров обновляется
    @Test
    void createNewTask() {
        Task task1 = new Task("Task1", "Task1 description", Status.NEW);
        taskManager.createTask(task1);
        Task createdTask = taskManager.getTaskById(task1.getId());
        assertNotNull(createdTask, "Task not found");
        assertEquals(task1, createdTask, "Tasks are not equal");
        List<Task> tasksList = taskManager.getAllTasks();
        assertNotNull(tasksList, "Tasks not found");
        assertEquals(1, tasksList.size(), "TaskList has different size");
        List<Task> tasksBefore = taskManager.getAllTasks();
        assertEquals(1, tasksBefore.size(), "Incorrect number of tasks before creation");
        Task task2 = new Task("Task2", "Task2 description", Status.IN_PROGRESS);
        taskManager.createTask(task2);
        List<Task> tasksAfter = taskManager.getAllTasks();
        assertEquals(2, tasksAfter.size(), "Incorrect number of tasks after creation");
        Task secondCreatedTask = taskManager.getTaskById(task2.getId());
        List<Task> tasksHistory = taskManager.getHistory();
        assertNotNull(tasksHistory, "History not found");
        assertEquals(2, tasksHistory.size(), "Incorrect number of entries in history");
        assertEquals(task1, tasksHistory.get(0), "Incorrect first entry in history");
        assertEquals(task2, tasksHistory.get(1), "Incorrect second entry in history");
    }

    @Test
    void createNewEpic() {
        Epic epic1 = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic1);
        Epic createdEpic = taskManager.getEpicById(epic1.getId());
        assertNotNull(createdEpic, "Epic not found");
        assertEquals(epic1, createdEpic, "Epics are not equal");
        List<Epic> epicsList = taskManager.getAllEpics();
        assertNotNull(epicsList, "Epics not found");
        assertEquals(1, epicsList.size(), "EpicsList has different size");
        List<Task> epicsHistory = taskManager.getHistory();
        assertNotNull(epicsHistory, "History not found");
        assertEquals(1, epicsHistory.size(), "Incorrect number of entries in history");
        assertEquals(epic1, epicsHistory.get(0), "Incorrect first entry in history");
    }

    @Test
    void createNewSubtask() {
        Epic epic1 = new Epic("First epic", "First epic description");
        taskManager.createEpic(epic1);
        Subtask subtask1InEpic1 = new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic1.getId());
        taskManager.createSubtask(subtask1InEpic1);
        Task createdSubtask = taskManager.getSubtaskById(subtask1InEpic1.getId());
        assertNotNull(createdSubtask, "Subtask not found");
        assertEquals(subtask1InEpic1, createdSubtask, "Subtasks are not equal");
        List<Subtask> subtasksList = taskManager.getAllSubtasks();
        assertNotNull(subtasksList, "Subtasks not found");
        assertEquals(1, subtasksList.size(), "SubtasksList has different size");
    }
    // Проверка, что экземпляры классов равны друг другу, если равен их id (очень странная проверка, ведь мы генерируем
    // уникальный id для всех экземпляров)
    @Test
    void shouldBeEqualsTasks(){
        Task task1 = new Task("Task1", "Task1 description", Status.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("Task1", "Task1 description", Status.NEW);
        task2.setId(1);
        Task taskToCheck = taskManager.getTaskById(task1.getId());
        assertEquals(task2.getId(), taskToCheck.getId(), "Different id");
        assertEquals(task2.getTitle(), taskToCheck.getTitle(), "Different title");
        assertEquals(task2.getDescription(), taskToCheck.getDescription(), "Different description");
        assertEquals(task2.getStatus(), taskToCheck.getStatus(), "Different status");
    }
    @Test
    void shouldBeEqualsEpics() {
        Epic epic1 = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic1);
        Subtask subtask1InEpic1 = new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic1.getId());
        taskManager.createSubtask(subtask1InEpic1);

        Epic epic2 =  new Epic("Epic", "Epic description");
        epic2.setId(1);
        epic2.addSubtask(subtask1InEpic1.getId(), subtask1InEpic1);
        Epic epicToCheck = taskManager.getEpicById(epic2.getId());
        assertEquals(epic2.getId(), epicToCheck.getId(),
                "Different id");
        assertEquals(epic2.getTitle(), epicToCheck.getTitle(),
                "Different title");
        assertEquals(epic2.getDescription(), epicToCheck.getDescription(),
                "Different description");
        assertEquals(epic2.getStatus(), epicToCheck.getStatus(),
                "Different status");
        assertEquals(epic2.getSubtasks(), epicToCheck.getSubtasks(),
                "Different subtasks");
    }
    @Test
    void shouldBeEqualsSubtasks(){
        Epic epic1 = new Epic("First epic", "First epic description");
        taskManager.createEpic(epic1);
        Subtask subtask1InEpic1 = new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic1.getId());
        taskManager.createSubtask(subtask1InEpic1);
        Subtask subtask2InEpic1 =   new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic1.getId());
        subtask2InEpic1.setId(2);
        Subtask subtaskToCheck = taskManager.getSubtaskById(subtask1InEpic1.getId());
        assertEquals(subtask2InEpic1.getId(), subtaskToCheck.getId(), "Different id");
        assertEquals(subtask2InEpic1.getTitle(), subtaskToCheck.getTitle(), "Different title");
        assertEquals(subtask2InEpic1.getDescription(), subtaskToCheck.getDescription(), "Different description");
        assertEquals(subtask2InEpic1.getStatus(), subtaskToCheck.getStatus(), "Different status");
        assertEquals(subtask2InEpic1.getEpicId(), subtaskToCheck.getEpicId(), "Different epic id");
    }



}









