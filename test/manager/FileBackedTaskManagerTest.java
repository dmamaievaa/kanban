package manager;

import exceptions.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    @BeforeEach
    void preparation() {
        taskManager = Managers.getDefaultFileBackend();
    }

    private File createTempFileWithTasks() throws IOException {
        File file = File.createTempFile("tasks", ".csv");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,title,status,description,epicId\n");
            writer.write("1,TASK,Task,NEW,Task description\n");
            writer.write("2,EPIC,Epic,NEW,Epic description\n");
            writer.write("3,SUBTASK,Subtask,DONE,Subtask description,2\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    @DisplayName("Should create new Task")
    @Test
    void shouldCreateNewTaskFromFile() throws IOException, ManagerSaveException {
        File file = createTempFileWithTasks();
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);

        assertEquals(1, manager.tasks.size());
        Task task = manager.tasks.get(1);
        assertNotNull(task);
        assertEquals("Task", task.getTitle());
        assertEquals(Status.NEW, task.getStatus());
    }

    @DisplayName("Should create new Subtask")
    @Test
    void shouldCreateNewSubtaskFromFile() throws IOException, ManagerSaveException {
        File file = createTempFileWithTasks();
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(1, manager.subtasks.size());
        Subtask subtask = manager.subtasks.get(3);
        assertNotNull(subtask);
        assertEquals("Subtask", subtask.getTitle());
        assertEquals(Status.DONE, subtask.getStatus());
        assertEquals(2, subtask.getEpicId());
    }

    @DisplayName("Should create new Epic")
    @Test
    void shouldCreateNewEpicFromFile() throws IOException, ManagerSaveException {
        File file = createTempFileWithTasks();
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(1, manager.epics.size());
        Epic epic = manager.epics.get(2);
        assertNotNull(epic);
        assertEquals("Epic", epic.getTitle());
        assertEquals(Status.DONE, epic.getStatus());
        assertFalse(epic.getSubtasks().isEmpty());
    }

    @DisplayName("Epic should contain Subtask")
    @Test
    void shouldEpicContainSubtask() throws IOException, ManagerSaveException {
        File file = createTempFileWithTasks();
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);

        Epic epic = manager.epics.get(2);
        assertNotNull(epic);
        assertEquals(1, epic.getSubtasks().size());
        assertTrue(epic.getSubtasks().containsKey(3));
    }

    @DisplayName("Should add task to history")
    @Test
    void shouldAddToHistory() throws IOException, ManagerSaveException {
        File file = createTempFileWithTasks();
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);

        Task task = manager.tasks.get(1);
        manager.getTaskById(task.getId());
        List<Task> history = manager.getHistory();
        assertEquals(1, history.size());
        assertTrue(history.contains(task));
    }

    @DisplayName("Create and check empty file")
    @Test
    void shouldCreateEmptyFile() throws IOException, ManagerSaveException {
        File emptyFile = File.createTempFile("empty", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(emptyFile);
        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(emptyFile);
        assertTrue(loadedManager.getAllTasks().isEmpty());
        assertTrue(loadedManager.getAllEpics().isEmpty());
        assertTrue(loadedManager.getAllSubtasks().isEmpty());
    }
}
