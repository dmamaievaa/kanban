package manager;

import task.Status;
import task.Task;
import task.Epic;
import task.Subtask;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Subtask> subtasks;
    private final Map<Integer, Epic> epics;
    private final HistoryManager historyManager;
    private int id = 1;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void createTask(Task task) {
        task.setId(id);
        tasks.put(id, task);
        generateId();
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else System.out.println("updateTask: Task with ID " + task.getId() + " not found");
    }

    @Override
    public void removeTaskById(int taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void removeAllTasks() {
        for (int id : tasks.keySet()) {
            removeTaskById(id);
        }
    }

    // Methods for epics
    @Override
    public void createEpic(Epic epic) {
        epic.setId(id);
        epics.put(id, epic);
        generateId();
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic current = epics.get(epic.getId());
            if (current == null) {
                return;
            }
            current.setTitle(epic.getTitle());
            current.setDescription(epic.getDescription());
        } else {
            System.out.println("Epic with ID " + epic.getId() + " not found");
        }
    }

    public void findEpicStatus(Epic epic) {
        HashMap<Integer, Subtask> epicSubtasksMap = epic.getSubtasks();
        if (epicSubtasksMap.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        int doneSubtasksCounter = 0;
        int newSubtasksCounter = 0;
        for (Subtask subtask : epicSubtasksMap.values()) {
            if (subtask != null && subtask.getEpicId() == epic.getId()) {
                Status status = subtask.getStatus();
                switch (status) {
                    case NEW:
                        newSubtasksCounter += 1;
                        break;
                    case DONE:
                        doneSubtasksCounter += 1;
                        break;
                    default:
                        epic.setStatus(Status.IN_PROGRESS);
                        return;
                }
            }
        }
        if (doneSubtasksCounter == epicSubtasksMap.size()) {
            epic.setStatus(Status.DONE);
        } else if (newSubtasksCounter == epicSubtasksMap.size()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void removeEpicById(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.remove(epicId);
            historyManager.remove(epicId);
            HashMap<Integer, Subtask> epicSubtasks = epic.getSubtasks();
            for (Integer subtaskId : epicSubtasks.keySet()) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);

            }
        } else {
            System.out.println("Epic with ID " + epicId + " not found");
        }
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void removeAllEpics() {
        for (int epicId : epics.keySet()) {
            removeEpicById(epicId);
        }
        epics.clear();
        removeAllSubtasks();
    }

    // Methods for subtasks
    @Override
    public void createSubtask(Subtask subtask) {
        subtasks.put(id, subtask);
        subtask.setId(id);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(id, subtask);
            generateId();
            findEpicStatus(epic);
        } else {
            System.out.println("Epic with ID " + subtask.getEpicId() + " not found.");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int subtaskId = subtask.getId();
        if (subtasks.containsKey(subtaskId)) {
            subtasks.put(subtaskId, subtask);
            Epic epicToUpdate = null;
            for (Epic epic : epics.values()) {
                if (epic.getSubtasks().containsValue(subtask)) {
                    epicToUpdate = epic;
                    epicToUpdate.getSubtasks().put(subtaskId, subtask);
                    break;
                }
            }
            if (epicToUpdate != null) {
                findEpicStatus(epicToUpdate);
            }
        } else System.out.println("Subtask with id " + subtaskId + " not found");
    }

    @Override
    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
            historyManager.remove(id);
            for (Epic epic : epics.values()) {
                if (epic.getSubtasks().containsValue(subtask)) {
                    epic.getSubtasks().remove(id, subtask);
                    findEpicStatus(epic);
                    break;
                }
            }
        } else {
            System.out.println("Subtask with ID " + id + " not found.");
        }
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            findEpicStatus(epic);
        }
        subtasks.clear();
    }

    public void generateId() {
        id++;
    }


    public void printAllInstances() {
        System.out.println("Tasks:");
        System.out.println(getAllTasks());
        System.out.println("Subtasks");
        System.out.println(getAllSubtasks());
        System.out.println("Epics");
        System.out.println(getAllEpics());
    }

}
