package server;

import adapters.DurationAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import exceptions.NotFoundException;
import exceptions.TaskValidationException;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

public class HttpTaskServer {
    private static final int port = 8080;
    private static final String tasksPath = "/tasks";
    private static final String subtasksPath = "/subtasks";
    private static final String epicsPath = "/epics";
    private static final String historyPath = "/history";
    private static final String prioritizedPath = "/prioritized";
    private static final String NOT_FOUND_MSSG = "Not Found";
    private static final String NOT_ACCEPT_MSSG = "Not Acceptable";
    private static final String SERVER_ERR_MSSG = "Internal Server Error";
    private static final Gson gson = getGson();
    private HttpServer httpServer;
    private TaskManager taskManager;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(port), 1);
        this.httpServer.createContext(tasksPath, new TasksHandler());
        this.httpServer.createContext(subtasksPath, new SubtasksHandler());
        this.httpServer.createContext(epicsPath, new EpicsHandler());
        this.httpServer.createContext(historyPath, new HistoryHandler());
        this.httpServer.createContext(prioritizedPath, new PrioritizedHandler());
        this.taskManager = manager;
    }

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 'stop' to shutdown the server");
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("stop")) {
                httpTaskServer.stop();
                break;
            }
        }
        scanner.close();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    public void start() {
        httpServer.start();
        System.out.printf("HTTP server is running on port %d%n", port);
    }

    public void stop() {
        httpServer.stop(1);
    }

    class TasksHandler extends BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException, NotFoundException,
                TaskValidationException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            InputStream inputStreamTask = exchange.getRequestBody();
            String taskBody = new String(inputStreamTask.readAllBytes(), StandardCharsets.UTF_8);
            String[] splitStrings = path.split("/");
            switch (method) {
                case "GET":
                    if (path.endsWith(tasksPath)) {
                        sendOkResponse(exchange, taskManager.getAllTasks().toString());
                    } else {
                        try {
                            int id = Integer.parseInt(splitStrings[2]);
                            sendOkResponse(exchange, taskManager.getTaskById(id).toString());
                        } catch (NotFoundException e) {
                            sendResourceNotFound(exchange, NOT_FOUND_MSSG);
                        }
                    }
                    break;
                case "POST":
                    Task task = gson.fromJson(taskBody, Task.class);
                    if (task.getId() == null) {
                        try {
                            taskManager.createTask(task);
                            sendCreatedResponse(exchange, "Task was created");
                        } catch (TaskValidationException e) {
                            sendTaskOverlaps(exchange, NOT_ACCEPT_MSSG);
                        }
                    } else {
                        taskManager.updateTask(task);
                        sendCreatedResponse(exchange, "Task was updated");
                    }
                    break;
                case "DELETE":
                    if (splitStrings.length > 2) {
                        Integer taskId = Integer.parseInt(splitStrings[2]);
                        try {
                            taskManager.removeTaskById(taskId);
                            sendOkResponse(exchange, "Task was deleted");
                        } catch (NotFoundException e) {
                            sendResourceNotFound(exchange, NOT_FOUND_MSSG);
                        }
                    } else {
                        sendResourceNotFound(exchange, NOT_FOUND_MSSG);
                    }
                    break;
                default:
                    sendResourceNotFound(exchange, NOT_FOUND_MSSG);
            }

        }
    }

    class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException, NotFoundException,
                TaskValidationException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            InputStream inputStreamTask = exchange.getRequestBody();
            String taskBody = new String(inputStreamTask.readAllBytes(), StandardCharsets.UTF_8);
            String[] splitStrings = path.split("/");
            switch (method) {
                case "GET":
                    if (path.endsWith(subtasksPath)) {
                        sendOkResponse(exchange, taskManager.getAllSubtasks().toString());
                    } else {
                        try {
                            int id = Integer.parseInt(splitStrings[2]);
                            sendOkResponse(exchange, taskManager.getSubtaskById(id).toString());
                        } catch (NotFoundException e) {
                            sendResourceNotFound(exchange, NOT_FOUND_MSSG);
                        }
                    }

                    break;
                case "POST":
                    Subtask subtask = gson.fromJson(taskBody, Subtask.class);
                    if (subtask.getId() == null) {
                        try {
                            taskManager.createSubtask(subtask);
                            sendCreatedResponse(exchange, "Subtask was created");
                        } catch (TaskValidationException e) {
                            sendTaskOverlaps(exchange, NOT_ACCEPT_MSSG);
                        }
                    } else {
                        taskManager.updateSubtask(subtask);
                        sendCreatedResponse(exchange, "Subtask was updated");
                    }
                    break;
                case "DELETE":
                    int subtaskId = Integer.parseInt(splitStrings[2]);
                    try {
                        taskManager.removeSubtaskById(subtaskId);
                        sendOkResponse(exchange, "Subtask was deleted");
                    } catch (NotFoundException e) {
                        sendResourceNotFound(exchange, NOT_FOUND_MSSG);
                    }
                    break;
                default:
                    sendResourceNotFound(exchange, NOT_FOUND_MSSG);
            }
        }
    }

    public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            InputStream inputStreamTask = exchange.getRequestBody();
            String taskBody = new String(inputStreamTask.readAllBytes(), StandardCharsets.UTF_8);
            String[] splitStrings = path.split("/");
            try {
                switch (method) {
                    case "GET":
                        if (path.endsWith("/subtasks")) {
                            int epicId = Integer.parseInt(splitStrings[2]);
                            Epic epic = taskManager.getEpicById(epicId);
                            if (epic != null && epic.getSubtasks() != null) {
                                sendOkResponse(exchange, gson.toJson(epic.getSubtasks()));
                            } else {
                                sendResourceNotFound(exchange, NOT_FOUND_MSSG);
                            }
                        } else {
                            sendResourceNotFound(exchange, "Endpoint not found");
                        }
                        break;
                    case "POST":
                        Epic epic = gson.fromJson(taskBody, Epic.class);
                        if (epic.getId() == null) {
                            taskManager.createEpic(epic);
                            sendCreatedResponse(exchange, "Epic was created");
                        } else {
                            taskManager.updateEpic(epic);
                            sendCreatedResponse(exchange, "Epic was updated");
                        }
                        break;
                    case "DELETE":
                        int epicId = Integer.parseInt(splitStrings[2]);
                        taskManager.removeEpicById(epicId);
                        sendOkResponse(exchange, "Epic was deleted");
                        break;
                    default:
                        sendResourceNotFound(exchange, NOT_FOUND_MSSG);
                }
            } catch (NotFoundException e) {
                sendResourceNotFound(exchange, NOT_FOUND_MSSG);
            } catch (Exception e) {
                sendInternalServerError(exchange, SERVER_ERR_MSSG);
            }
        }
    }

    public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException, NotFoundException {
            String method = exchange.getRequestMethod();
            if (method.equals("GET")) {
                sendOkResponse(exchange, taskManager.getHistory().toString());
            } else {
                sendResourceNotFound(exchange, NOT_FOUND_MSSG);
            }
        }
    }

    public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException, NotFoundException {
            String method = exchange.getRequestMethod();
            if (method.equals("GET")) {
                sendOkResponse(exchange, taskManager.getPrioritizedTasks().toString());
            } else {
                sendResourceNotFound(exchange, NOT_FOUND_MSSG);
            }
        }
    }
}