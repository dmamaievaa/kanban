package manager;

import exceptions.ManagerSaveException;
import exceptions.TaskValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task1;
    protected Task task2;
    protected Epic epic1;
    protected Epic epic2;
    protected Subtask subtask1;
    protected Subtask subtask2;

    @Nested
    @DisplayName("Task operations")
    class TaskOperations {
        @BeforeEach
        void preparation() {
            task1 = TestUtil.createFirstTask();
            task2 = TestUtil.createSecondTask();
        }
        @DisplayName("Create new task")
        @Test
        void shouldCreateNewTask() {
            taskManager.createTask(task1);
            Task createdTask = taskManager.getTaskById(task1.getId());
            assertNotNull(createdTask, "Task not found");
            assertEquals(task1, createdTask, "Tasks are not equal");
            List<Task> tasksList = taskManager.getAllTasks();
            assertNotNull(tasksList, "Tasks not found");
            assertEquals(1, tasksList.size(), "TaskList has different size");
        }

        @DisplayName("Check that tasks are equal")
        @Test
        void shouldBeEqualsTasks() {
            taskManager.createTask(task1);
            task2.setId(1);
            Task taskToCheck = taskManager.getTaskById(task1.getId());
            assertEquals(task2.getId(), taskToCheck.getId(), "Different id");
            assertEquals(task2.getTitle(), taskToCheck.getTitle(), "Different title");
            assertEquals(task2.getDescription(), taskToCheck.getDescription(),
                    "Different description");
            assertEquals(task2.getStatus(), taskToCheck.getStatus(), "Different status");
        }

        @DisplayName("Check task creation history")
        @Test
        void expectThatHistoryContainsCreatedTask() {
            taskManager.createTask(task1);
            Task createdTask = taskManager.getTaskById(task1.getId());
            List<Task> tasksHistory = taskManager.getHistory();
            assertNotNull(tasksHistory, "History not found");
            assertEquals(1, tasksHistory.size(),
                    "Incorrect number of entries in history");
            assertEquals(task1, tasksHistory.getFirst(),
                    "Incorrect first entry in history");
        }

        @DisplayName("Check task removal by id")
        @Test
        void shouldRemoveTaskById() {
            System.out.println(task1);
            taskManager.createTask(task1);
            taskManager.removeTaskById(task1.getId());
            assertFalse(taskManager.getAllTasks().contains(task1),
                    "Task wasn't deleted");
            assertFalse(taskManager.getHistory().contains(task1),
                    "Task wasn't deleted from history");
        }

        @DisplayName("Check task updates")
        @Test
        void checkUpdateTask() {
            taskManager.createTask(task1);
            task1.setTitle("Updated task");
            taskManager.updateTask(task1);
            Task updatedTask = taskManager.getTaskById(task1.getId());
            assertEquals("Updated task", updatedTask.getTitle(),
                    "Task title wasn't updated");
        }

        @DisplayName("No overlap for tasks")
        @Test
        void shouldntThrowTaskValidationExc() {
            taskManager.createTask(task1);
            taskManager.createTask(task2);
            assertDoesNotThrow(() -> taskManager.checkTaskTime(task2));
        }

        @DisplayName("Tasks overlap each other")
        @Test
        void shouldThrowTaskValidationExc() {
            taskManager.createTask(task1);
            Task task2 = TestUtil.createFirstTask();
            assertThrows(TaskValidationException.class, () -> taskManager.createTask(task2));
        }
    }

    @Nested
    @DisplayName("Epic operations")
    class EpicOperations {
        @BeforeEach
        void preparation() {
            epic1 = TestUtil.createFirstEpic();
            epic2 = TestUtil.createSecondEpic();
            subtask1 = TestUtil.createFirstSubtask(epic1);
            subtask2 = TestUtil.createSecondSubtask(epic2);
        }
        @DisplayName("Create new epic")
        @Test
        void shouldCreateNewEpic() {
            taskManager.createEpic(epic1);
            Epic createdEpic = taskManager.getEpicById(epic1.getId());
            assertNotNull(createdEpic, "Epic not found");
            assertEquals(epic1, createdEpic, "Epics are not equal");
            List<Epic> epicsList = taskManager.getAllEpics();
            assertNotNull(epicsList, "Epics not found");
            assertEquals(1, epicsList.size(), "EpicsList has different size");
        }

        @DisplayName("Check that epics are equal")
        @Test
        void shouldBeEqualsEpics() {
            taskManager.createEpic(epic1);
            taskManager.createSubtask(subtask1);
            Epic epic2 = new Epic("Epic", "Epic description");
            epic2.setId(1);
            epic2.addSubtask(subtask1.getId(), subtask1);
            Epic epicToCheck = taskManager.getEpicById(epic2.getId());
            assertEquals(epic2.getId(), epicToCheck.getId(), "Different id");
            assertEquals(epic2.getTitle(), epicToCheck.getTitle(), "Different title");
            assertEquals(epic2.getDescription(), epicToCheck.getDescription(),
                    "Different description");
            assertEquals(epic2.getStatus(), epicToCheck.getStatus(),
                    "Different status");
            assertEquals(epic2.getSubtasks(), epicToCheck.getSubtasks(),
                    "Different subtasks");
        }

        @DisplayName("Check epic creation history")
        @Test
        void expectThatHistoryContainsCreatedEpic() {
            taskManager.createEpic(epic1);
            Epic epicFromHistory = taskManager.getEpicById(epic1.getId());
            List<Task> epicsHistory = taskManager.getHistory();
            assertNotNull(epicsHistory, "History not found");
            assertEquals(1, epicsHistory.size(),
                    "Incorrect number of entries in history");
            assertEquals(epicFromHistory, epicsHistory.getFirst(),
                    "Incorrect first entry in history");
        }

        @DisplayName("Check epic removal by id")
        @Test
        void shouldRemoveEpicById() {
            taskManager.createEpic(epic1);
            taskManager.createSubtask(subtask1);
            taskManager.removeEpicById(epic1.getId());
            assertFalse(taskManager.getAllEpics().contains(epic1),
                    "Epic wasn't deleted");
            assertFalse(taskManager.getAllSubtasks().contains(subtask1),
                    "Subtask wasn't deleted");
            assertFalse(taskManager.getHistory().contains(epic1),
                    "Epic wasn't deleted from history");
            assertFalse(taskManager.getHistory().contains(subtask1),
                    "Subtask wasn't deleted from history");
        }

        @DisplayName("Remove all epics")
        @Test
        void shouldRemoveAllEpics() {
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
            taskManager.createEpic(epic1);
            epic1.setTitle("New title");
            epic1.setDescription("New description");
            taskManager.updateEpic(epic1);
            Epic updatedEpic = taskManager.getEpicById(epic1.getId());
            assertNotNull(updatedEpic, "Epic with id " + epic1.getId() + " not found");
            assertEquals(epic1.getTitle(), updatedEpic.getTitle(), "Title wasn't updated");
            assertEquals(epic1.getDescription(), updatedEpic.getDescription(),
                    "Description wasn't updated");
            assertTrue(taskManager.getAllEpics().contains(updatedEpic),
                    "Updated epic not found in the list of all epics");
        }

        @DisplayName("Calc start/end time/duration of epic with subtasks")
        @Test
        void shouldCalculateEpicTimeBasedOnSubtasks() {
            taskManager.createEpic(epic1);
            taskManager.createSubtask(subtask1);
            Subtask subtask2InEpic1 = TestUtil.createSecondSubtask(epic1);
            taskManager.createSubtask(subtask2InEpic1);
            taskManager.calcEpicEndTime(epic1);
            System.out.println(epic1.getStartTime());
            System.out.println(epic1.getEndTime());
            assertEquals(LocalDateTime.of(2024, 6, 5, 10, 0),
                    epic1.getStartTime());
            assertEquals(LocalDateTime.of(2024, 6, 7, 11, 0),
                    epic1.getEndTime());
            assertEquals(Duration.ofHours(3), epic1.getDuration());
        }

        @DisplayName("Calc start/end time/duration of epic without subtasks")
        @Test
        void shouldCalculateEpicTimeWithoutSubtasks() {
            taskManager.createEpic(epic1);
            taskManager.calcEpicEndTime(epic1);
            assertNull(epic1.getStartTime());
            assertNull(epic1.getEndTime());
            assertEquals(Duration.ZERO, epic1.getDuration());
        }
    }

    @Nested
    @DisplayName("Subtask operations")
    class SubtaskOperations {
        @BeforeEach
        void preparation() {
            epic1 = TestUtil.createFirstEpic();
            epic2 = TestUtil.createSecondEpic();
            subtask1 = TestUtil.createFirstSubtask(epic1);
            subtask2 = TestUtil.createSecondSubtask(epic2);
        }
        @DisplayName("Create new subtask")
        @Test
        void shouldCreateNewSubtask() {
            taskManager.createEpic(epic1);
            taskManager.createSubtask(subtask1);
            Task createdSubtask = taskManager.getSubtaskById(subtask1.getId());
            assertNotNull(createdSubtask, "Subtask not found");
            assertEquals(subtask1, createdSubtask, "Subtasks are not equal");
            List<Subtask> subtasksList = taskManager.getAllSubtasks();
            assertNotNull(subtasksList, "Subtasks not found");
            assertEquals(1, subtasksList.size(),
                    "SubtasksList has different size");
        }

        @DisplayName("Check that subtasks are equal")
        @Test
        void shouldBeEqualsSubtasks() {
            taskManager.createEpic(epic1);
            taskManager.createSubtask(subtask1);
            Subtask subtask2 = TestUtil.createFirstSubtask(epic1);
            subtask2.setId(2);
            Subtask subtaskToCheck = taskManager.getSubtaskById(subtask1.getId());
            assertEquals(subtask2.getId(), subtaskToCheck.getId(), "Different id");
            assertEquals(subtask2.getTitle(), subtaskToCheck.getTitle(),
                    "Different title");
            assertEquals(subtask2.getDescription(), subtaskToCheck.getDescription(),
                    "Different description");
            assertEquals(subtask2.getStatus(), subtaskToCheck.getStatus(),
                    "Different status");
            assertEquals(subtask2.getEpicId(), subtaskToCheck.getEpicId(),
                    "Different epic id");
        }

        @DisplayName("Check subtask removal by id")
        @Test
        void shouldRemoveSubtaskById() {
            taskManager.createEpic(epic1);
            Subtask subtask1InEpic1 = TestUtil.createFirstSubtask(epic1);
            taskManager.createSubtask(subtask1InEpic1);
            taskManager.removeSubtaskById(subtask1InEpic1.getId());
            assertFalse(taskManager.getAllSubtasks().contains(subtask1InEpic1),
                    "Subtask wasn't deleted");
            assertFalse(taskManager.getHistory().contains(subtask1InEpic1),
                    "Subtask wasn't deleted from history");
        }
    }

    @Nested
    @DisplayName("Tasks from file")
    class OperationsWithTasksFromFile {
        @DisplayName("Create tasks from string and compare with manually created")
        @Test
        void shouldConvertStringToTaskAndCompare() throws ManagerSaveException {
            String input = """
                    1,TASK,Task1,NEW,Task1 description,
                    2,EPIC,Epic1,NEW,Epic1 description,
                    3,SUBTASK,Subtask1,DONE,Subtask1 description,2""";
            String[] lines = input.split("\\r?\\n");
            Task task1 = TaskConverter.fromString(lines[0]);
            Task task2 = new Task("Task1", "Task1 description", Status.NEW,
                    LocalDateTime.of(2024, 6, 4, 10, 0),
                    Duration.ofHours(1));
            taskManager.createTask(task2);
            assertEquals(task1, task2);
            Task task3 = TaskConverter.fromString(lines[1]);
            Epic epic = new Epic("Epic1", "Epic1 description");
            taskManager.createEpic(epic);
            assertEquals(task3, epic);
            Task task4 = TaskConverter.fromString(lines[2]);
            Subtask subtask1 = TestUtil.createFirstSubtask(epic);
            taskManager.createSubtask(subtask1);
            assertEquals(task4, subtask1);
        }
    }
}