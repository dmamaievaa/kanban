package server;

import com.google.gson.Gson;
import manager.Managers;
import manager.TestUtil;
import manager.TaskManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private static final String TASKS_URL = "http://localhost:8080/tasks";
    private static final String SUBTASKS_URL = "http://localhost:8080/subtasks";
    private static final String EPICS_URL = "http://localhost:8080/epics";
    private static final String HISTORY_URL = "http://localhost:8080/history";
    private static final String PRIORITIZED_URL = "http://localhost:8080/prioritized";

    protected TaskManager manager = Managers.getDefault();
    private HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();
    protected Task task1;
    protected Task task2;
    protected Epic epic1;
    protected Subtask subtask1;

    public HttpTaskServerTest() throws IOException {}

    @BeforeEach
    public void preparation() {
        taskServer.start();
        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();
        epic1 = TestUtil.createFirstEpic();
        manager.createEpic(epic1);
        subtask1 = TestUtil.createFirstSubtask(epic1);
        manager.createSubtask(subtask1);
        task1 = TestUtil.createFirstTask();
        manager.createTask(task1);
        task2 = TestUtil.createSecondTask();
        manager.createTask(task2);
        manager.getTaskById(task2.getId());
    }

    @AfterEach
    public void stop() {
        taskServer.stop();
    }
    @Nested
    @DisplayName("TasksHandler")
    class TasksHandlerTest {
        @Test
        void shouldGetTask() throws IOException, InterruptedException {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(TASKS_URL);
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse < String > response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            List < Task > tasksFromManager = manager.getAllTasks();
            assertNotNull(tasksFromManager);
            assertEquals(2, tasksFromManager.size());
        }
        @Test
        void shouldAddTask() throws IOException, InterruptedException {
            String taskJson = gson.toJson(task1);
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(TASKS_URL);
            HttpRequest request = HttpRequest.newBuilder().uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
            HttpResponse < String > response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());
            List < Task > tasksFromManager = manager.getAllTasks();
            assertNotNull(tasksFromManager, "Tasks are not returned");
            assertEquals(2, tasksFromManager.size());
            Task addedTask = tasksFromManager.stream().filter(t ->
                    t.getTitle().equals("Task1")).findFirst().orElse(null);
            assertEquals("Task1", addedTask.getTitle());
        }
        @Test
        public void shouldDeleteTask() throws IOException, InterruptedException {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(TASKS_URL + "/" + task2.getId());
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse < String > response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            List < Task > tasksFromManager = manager.getAllTasks();
            assertEquals(200, response.statusCode(), "Expected 200 OK");
            assertThrows(NullPointerException.class, () -> manager.getTaskById(task2.getId()));
        }
        @Test
        void shouldReturnResourceNotFound() throws IOException, InterruptedException {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create("http://localhost:8080/nonexistent-endpoint");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse < String > response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
        }
    }

    @Nested
    @DisplayName("Subtasks Handler")
    class SubtasksHandlerTest {
        @Test
        void shouldntAddSubtaskWithOverlapping() throws IOException, InterruptedException {
            Subtask subtask1Overlaps = new Subtask("Overlap subtask in epic 1",
                    "First subtask description", Status.NEW, epic1.getId(),
                    LocalDateTime.of(2024, 6, 7, 10, 0),
                    Duration.ofHours(1));
            String taskJson = gson.toJson(subtask1Overlaps);
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(SUBTASKS_URL);
            HttpRequest request = HttpRequest.newBuilder().uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
            HttpResponse < String > response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(406, response.statusCode());
            assertEquals(response.body(), "Not Acceptable");
        }
        @Test
        void shouldUpdateSubtask() throws IOException, InterruptedException {
            Subtask updatedSubtask = new Subtask(2, "Subtask 1 updated",
                    "First subtask description", Status.NEW, epic1.getId(),
                    LocalDateTime.of(2024, 6, 7, 10, 0),
                    Duration.ofHours(2));
            String subtaskJson = gson.toJson(updatedSubtask);
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(SUBTASKS_URL);
            HttpRequest request = HttpRequest.newBuilder().uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
            HttpResponse < String > response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());
            List < Subtask > subtasksFromManager = manager.getAllSubtasks();
            assertNotNull(subtasksFromManager);
            assertEquals(1, subtasksFromManager.size());
            assertEquals("Subtask 1 updated", subtasksFromManager.get(0).getTitle());
        }

    }
    @Nested
    @DisplayName("Epics Handler")
    class EpicsHandlerHandlerTest {
        @Test
        void shouldGetEpicsSubtask() throws IOException, InterruptedException {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(EPICS_URL + "/" + epic1.getId() + "/subtasks");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse < String > response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals(1, epic1.getSubtasks().size());
            assertNotNull(epic1.getSubtasks());

        }
        @Test
        void shouldDeleteEpicByID() throws IOException, InterruptedException {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(EPICS_URL + "/" + epic1.getId());
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse < String > response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertNotNull(manager.getAllEpics());
            assertNotNull(epic1.getSubtasks());
            assertEquals(0, manager.getAllEpics().size());
        }
    }

    @Nested
    @DisplayName("History Handler")
    class HistoryHandlerTest {
        @Test
        void shouldReturnHistory() throws IOException, InterruptedException {
            manager.getTaskById(task1.getId());
            manager.getTaskById(task2.getId());
            manager.getEpicById(epic1.getId());
            manager.getSubtaskById(subtask1.getId());
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(HISTORY_URL);
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse < String > response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            List < Task > historyFromManager = manager.getHistory();
            assertNotNull(historyFromManager);
            assertEquals(response.body(), historyFromManager.toString());
        }
    }

    @Nested
    @DisplayName("Prioritized Handler")
    class PrioritizedHandlerTest {
        @Test
        void shouldReturnHistory() throws IOException, InterruptedException {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(PRIORITIZED_URL);
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse < String > response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            List < Task > prioritizedFromManager = manager.getPrioritizedTasks();
            assertNotNull(prioritizedFromManager);
            assertEquals(response.body(), prioritizedFromManager.toString());
        }
    }
}