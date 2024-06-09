package manager;

import exceptions.TaskValidationException;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Subtask> subtasks;
    protected final Map<Integer, Epic> epics;
    private final HistoryManager historyManager;
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));

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
        checkTaskTime(task);
        tasks.put(id, task);
        generateId();
        prioritizedTasks.add(task);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            checkTaskTime(task);
            tasks.put(task.getId(), task);
            prioritizedTasks.remove(task);
            prioritizedTasks.add(task);
        } else System.out.println("updateTask: Task with ID " + task.getId() + " not found");
    }

    @Override
    public void removeTaskById(Integer taskId) {
        prioritizedTasks.remove(getTaskById(taskId));
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public Task getTaskById(Integer taskId) {
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
        tasks.keySet().forEach(this::removeTaskById);
        prioritizedTasks.clear();
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
            calcEpicEndTime(epic);
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
        long newSubtasksCounter = epicSubtasksMap.values().stream().filter(subtask -> subtask
                != null && Objects.equals(subtask.getEpicId(), epic.getId()) && subtask.getStatus()
                == Status.NEW).count();
        long doneSubtasksCounter = epicSubtasksMap.values().stream().filter(subtask -> subtask
                != null && Objects.equals(subtask.getEpicId(), epic.getId())
                && subtask.getStatus() == Status.DONE).count();

        if (doneSubtasksCounter == epicSubtasksMap.size()) {
            epic.setStatus(Status.DONE);
        } else if (newSubtasksCounter == epicSubtasksMap.size()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void removeEpicById(Integer epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.remove(epicId);
            historyManager.remove(epicId);
            epic.getSubtasks().values().forEach(subtask -> {
                subtasks.remove(subtask.getId());
                historyManager.remove(subtask.getId());
                prioritizedTasks.remove(subtask);
            });
        } else {
            System.out.println("Epic with ID " + epicId + " not found");
        }
    }

    @Override
    public Epic getEpicById(Integer epicId) {
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
        for (Subtask subtask : subtasks.values()) {
            prioritizedTasks.remove(subtask);
        }
        removeAllSubtasks();
    }

    @Override
    public void calcEpicEndTime(Epic epic) {
        Duration totalDuration = Duration.ZERO;
        LocalDateTime epicStartTime = null;
        LocalDateTime epicEndTime = null;
        if (epic.getSubtasks() != null) {
            System.out.println(epic.getSubtasks());
            for (Subtask subtask : epic.getSubtasks().values()) {
                totalDuration = totalDuration.plus(subtask.getDuration());
                if (subtask.getStartTime() != null && (epicStartTime == null
                        || subtask.getStartTime().isBefore(epicStartTime))) {
                    epicStartTime = subtask.getStartTime();
                }
                if (subtask.getEndTime() != null && (epicEndTime == null
                        || subtask.getEndTime().isAfter(epicEndTime))) {
                    epicEndTime = subtask.getEndTime();
                }
            }
        }
        epic.setStartTime(epicStartTime);
        epic.setEndTime(epicEndTime);
        epic.setDuration(totalDuration);
    }

    // Methods for subtasks
    @Override
    public void createSubtask(Subtask subtask) {
        checkTaskTime(subtask);
        subtasks.put(id, subtask);
        subtask.setId(id);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(id, subtask);
            generateId();
            findEpicStatus(epic);
            prioritizedTasks.add(subtask);
            calcEpicEndTime(epic);
        } else {
            System.out.println("Epic with ID " + subtask.getEpicId() + " not found.");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int subtaskId = subtask.getId();
        if (subtasks.containsKey(subtaskId)) {
            checkTaskTime(subtask);
            prioritizedTasks.remove(subtask);
            prioritizedTasks.add(subtask);
            subtasks.put(subtaskId, subtask);
            epics.values().stream()
                    .filter(epic -> epic.getSubtasks().containsValue(subtask))
                    .findFirst()
                    .ifPresent(epicToUpdate -> {
                        epicToUpdate.getSubtasks().put(subtaskId, subtask);
                        findEpicStatus(epicToUpdate);
                    });
        } else {
            System.out.println("Subtask with id " + subtaskId + " not found");
        }
    }


    @Override
    public void removeSubtaskById(Integer id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
            historyManager.remove(id);
            for (Epic epic : epics.values()) {
                if (epic.getSubtasks().containsValue(subtask)) {
                    prioritizedTasks.remove(subtask);
                    epic.getSubtasks().remove(id, subtask);
                    findEpicStatus(epic);
                    calcEpicEndTime(epic);
                    break;
                }
            }
        } else {
            System.out.println("Subtask with ID " + id + " not found.");
        }
    }

    @Override
    public Subtask getSubtaskById(Integer subtaskId) {
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
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(Duration.ZERO);
        }
        for (Subtask subtask : subtasks.values()) {
            prioritizedTasks.remove(subtask);
        }
        subtasks.clear();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public void generateId() {
        id++;
    }

    @Override
    public void checkTaskTime(Task task) {
        if (prioritizedTasks.isEmpty()) {
            return;
        }
        LocalDateTime taskToAddStartTime = task.getStartTime();
        LocalDateTime taskToAddEndTime = task.getEndTime();

        prioritizedTasks.forEach(taskToCheck -> {
            if (Objects.equals(taskToCheck.getId(), task.getId())) {
                return;
            }
            LocalDateTime taskToCheckStartTime = taskToCheck.getStartTime();
            LocalDateTime taskToCheckEndTime = taskToCheck.getEndTime();
            if (!taskToAddEndTime.isAfter(taskToCheckStartTime)
                    || !taskToAddStartTime.isBefore(taskToCheckEndTime)) {
                return;
            }
            throw new TaskValidationException("Task intersects with task with id "
                    + taskToCheck.getId() + " from " + taskToCheckStartTime + " to "
                    + taskToCheckEndTime);
        });
    }
}








