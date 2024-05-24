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

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    TaskManager taskManager;

    @BeforeEach
    void preparation() {
        taskManager = Managers.getDefault();
    }

    @DisplayName("Create tasks from string and compare with manually created")
    @Test
    void shouldConvertStringToTaskAndCompare() throws ManagerSaveException {
        String input = """
                1,TASK,Task1,NEW,Task1 description,
                2,EPIC,Epic1,NEW,Epic1 description,
                3,SUBTASK,Subtask1,DONE,Subtask1 description,2""";
        String[] lines = input.split("\\r?\\n");
        Task task1 = TaskConverter.fromString(lines[0]);
        Task task2 = new Task("Task1", "Task1 description", Status.NEW);
        taskManager.createTask(task2);
        assertEquals(task1, task2);
        Task task3 = TaskConverter.fromString(lines[1]);
        Epic epic = new Epic("Epic1", "Epic1 description");
        taskManager.createEpic(epic);
        assertEquals(task3, epic);
        Task task4 = TaskConverter.fromString(lines[2]);
        Subtask subtask1 = new Subtask("Subtask1",
                "Subtask1 description", Status.NEW, task3.getId());
        taskManager.createSubtask(subtask1);
        assertEquals(task4, subtask1);
    }

    @DisplayName("Create and check different tasks")
    @Test
    void shouldCreateTasks() throws ManagerSaveException, IOException {
        File file = File.createTempFile("tasks", ".csv");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,title,status,description,epicId\n");
            writer.write("1,TASK,Task,NEW,Task description\n");
            writer.write("2,EPIC,Epic,NEW,Epic description\n");
            writer.write("3,SUBTASK,Subtask,DONE,Subtask description,2\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(1, manager.tasks.size());
        assertEquals(1, manager.epics.size());
        assertEquals(1, manager.subtasks.size());
        Task task = manager.tasks.get(1);
        assertNotNull(task);
        assertEquals("Task", task.getTitle());
        assertEquals(Status.NEW, task.getStatus());
        Subtask subtask = manager.subtasks.get(3);
        taskManager.createSubtask(subtask);
        assertNotNull(subtask);
        assertEquals("Subtask", subtask.getTitle());
        assertEquals(Status.DONE, subtask.getStatus());
        assertEquals(2, subtask.getEpicId());
        Epic epic = manager.epics.get(2);
        assertNotNull(epic);
        assertEquals("Epic", epic.getTitle());
        assertEquals(Status.DONE, epic.getStatus());
        assertEquals(1, epic.getSubtasks().size());
        assertTrue(epic.getSubtasks().containsKey(3));
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


