package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import task.Status;
import task.Task;
import task.Epic;
import task.Subtask;

import java.util.List;


@DisplayName("InMemoryTaskManagerTest")
public class InMemoryTaskManagerTest {
    TaskManager taskManager;

    @BeforeEach
    void preparation() {
        taskManager = Managers.getDefault();
    }

    @DisplayName("Create new task and check history")
    @Test
    void shouldCreateNewTask() {
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

    @DisplayName("Create new epic and check history")
    @Test
    void shouldCreateNewEpic() {
        Epic epic1 = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic1);
        Epic createdEpic = taskManager.getEpicById(epic1.getId());
        assertNotNull(createdEpic, "Epic not found");
        assertTrue(taskManager.getAllEpics().contains(epic1), "Epic wasn't created");
        assertTrue(taskManager.getHistory().contains(epic1), "Epic wasn't wasn't added to history");
        assertEquals(epic1, createdEpic, "Epics are not equal");
        List<Epic> epicsList = taskManager.getAllEpics();
        assertNotNull(epicsList, "Epics not found");
        assertEquals(1, epicsList.size(), "EpicsList has different size");
        List<Task> epicsHistory = taskManager.getHistory();
        assertNotNull(epicsHistory, "History not found");
        assertEquals(1, epicsHistory.size(), "Incorrect number of entries in history");
        assertEquals(epic1, epicsHistory.get(0), "Incorrect first entry in history");
    }

    @DisplayName("Create new subtask and check history")
    @Test
    void shouldCreateNewSubtask() {
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

    @DisplayName("Check that tasks are equal")
    @Test
    void shouldBeEqualsTasks() {
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

    @DisplayName("Check that epics are equal")
    @Test
    void shouldBeEqualsEpics() {
        Epic epic1 = new Epic("Epic", "Epic description");
        taskManager.createEpic(epic1);
        Subtask subtask1InEpic1 = new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic1.getId());
        taskManager.createSubtask(subtask1InEpic1);
        Epic epic2 = new Epic("Epic", "Epic description");
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

    @DisplayName("Check that subtasks are equal")
    @Test
    void shouldBeEqualsSubtasks() {
        Epic epic1 = new Epic("First epic", "First epic description");
        taskManager.createEpic(epic1);
        Subtask subtask1InEpic1 = new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic1.getId());
        taskManager.createSubtask(subtask1InEpic1);
        Subtask subtask2InEpic1 = new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic1.getId());
        subtask2InEpic1.setId(2);
        Subtask subtaskToCheck = taskManager.getSubtaskById(subtask1InEpic1.getId());
        assertEquals(subtask2InEpic1.getId(), subtaskToCheck.getId(), "Different id");
        assertEquals(subtask2InEpic1.getTitle(), subtaskToCheck.getTitle(), "Different title");
        assertEquals(subtask2InEpic1.getDescription(), subtaskToCheck.getDescription(), "Different description");
        assertEquals(subtask2InEpic1.getStatus(), subtaskToCheck.getStatus(), "Different status");
        assertEquals(subtask2InEpic1.getEpicId(), subtaskToCheck.getEpicId(), "Different epic id");
    }

    @DisplayName("Check task removal by id")
    @Test
    void shouldRemoveTaskById() {
        Task task1 = new Task("Task1", "Task1 description", Status.NEW);
        taskManager.createTask(task1);
        taskManager.removeTaskById(task1.getId());
        assertNotNull(task1.getId(), "Invalid id");
        assertFalse(taskManager.getAllTasks().contains(task1), "Task wasn't deleted");
        assertFalse(taskManager.getHistory().contains(task1), "Task wasn't deleted from history");
    }

    @DisplayName("Check epic removal by id")
    @Test
    void shouldRemoveEpicById() {
        Epic epic1 = new Epic("First epic", "First epic description");
        taskManager.createEpic(epic1);
        Subtask subtask1InEpic1 = new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic1.getId());
        taskManager.createSubtask(subtask1InEpic1);
        taskManager.removeEpicById(epic1.getId());
        assertFalse(taskManager.getAllEpics().contains(epic1), "Epic wasn't deleted");
        assertFalse(taskManager.getAllSubtasks().contains(subtask1InEpic1), "Subtask wasn't deleted");
        assertFalse(taskManager.getHistory().contains(epic1), "Epic wasn't deleted from history");
        assertFalse(taskManager.getHistory().contains(subtask1InEpic1), "Subtask wasn't deleted from history");

    }

    @DisplayName("Check subtask removal by id")
    @Test
    void shouldRemoveSubtaskById() {
        Epic epic1 = new Epic("First epic", "First epic description");
        taskManager.createEpic(epic1);
        Subtask subtask1InEpic1 = new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic1.getId());
        taskManager.createSubtask(subtask1InEpic1);
        taskManager.removeSubtaskById(subtask1InEpic1.getId());
        assertFalse(taskManager.getAllSubtasks().contains(subtask1InEpic1), "Subtask wasn't deleted");
        assertFalse(taskManager.getHistory().contains(subtask1InEpic1), "Subtask wasn't deleted from history");
    }

    @DisplayName("Remove all epics")
    @Test
    void shouldRemoveAllEpics() {
        Epic epic1 = new Epic(1, "First epic", "First epic description");
        taskManager.createEpic(epic1);
        taskManager.getEpicById(epic1.getId());
        taskManager.removeAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "Epics still present");
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Subtasks srill present");
        assertFalse(taskManager.getHistory().contains(epic1), "History is filled");
    }

    @DisplayName("Update epic")
    @Test
    void shouldUpdateEpic() {
        Epic epic1 = new Epic("First epic", "First epic description");
        taskManager.createEpic(epic1);
        epic1.setTitle("New title");
        epic1.setDescription("New description");
        taskManager.updateEpic(epic1);
        Epic updatedEpic = taskManager.getEpicById(epic1.getId());
        assertNotNull(updatedEpic, "Epic with id " + epic1.getId() + " not found");
        assertEquals(epic1.getTitle(), updatedEpic.getTitle(), "Title wasn't updated");
        assertEquals(epic1.getDescription(), updatedEpic.getDescription(), "Description wasn't updated");
        assertTrue(taskManager.getAllEpics().contains(updatedEpic), "Updated epic not found in the list of all epics");
    }

    @DisplayName("Update subtasks and epic status")
    @Test
    void shouldUpdateSubtasksAndEpicStatus() {
        Epic epic1 = new Epic("First epic", "First epic description");
        taskManager.createEpic(epic1);
        Subtask subtask1InEpic = new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic1.getId());
        taskManager.createSubtask(subtask1InEpic);
        Subtask subtaskInEpicUpdated = new Subtask(2, "Subtask in epic 4 after update",
                "Subtask in progress", Status.IN_PROGRESS, epic1.getId());
        taskManager.updateSubtask(subtaskInEpicUpdated);
        assertEquals(subtaskInEpicUpdated.getStatus(), Status.IN_PROGRESS, "Subtask status wasn't updated");
        assertEquals(epic1.getStatus(), Status.IN_PROGRESS, "Epic status wasn't updated");
    }
}


