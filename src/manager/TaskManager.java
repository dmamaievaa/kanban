package manager;

import task.Task;
import task.Epic;
import task.Subtask;
import task.Status;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;

public class TaskManager {
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Subtask> subtasks;
    protected HashMap<Integer, Epic> epics;
    protected int id = 1;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Methods for tasks
    public void createTask(Task task) {
        task.setId(id);
        tasks.put(id, task);
        generateId();
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else  System.out.println("updateTask: Task with ID " + task.getId() + " not found");
    }

    public void removeTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public Task getTaskById(int taskId) {
        return tasks.get(taskId);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    // Methods for epics
    public void createEpic(Epic epic) {
        epic.setId(id);
        epics.put(id, epic);
        generateId();
    }

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

    private void findEpicStatus(Epic epic) {
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

    public void removeEpicById(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.remove(epicId);
            HashMap<Integer, Subtask> epicSubtasks = epic.getSubtasks();
            for (Integer subtaskId : epicSubtasks.keySet()) {
                subtasks.remove(subtaskId);
            }
        } else {
            System.out.println("Epic with ID " + epicId + " not found");
        }
    }

    public Epic getEpicById(int epicId) {
        return epics.get(epicId);
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void removeAllEpics() {
        HashSet<Integer> epicIds = new HashSet<>(epics.keySet());
        for (int epicId : epicIds) {
            removeEpicById(epicId);
        }
        epics.clear();
        removeAllSubtasks();
    }

    // Methods for subtasks
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

    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
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

    public Subtask getSubtaskById(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtasks().clear();
            findEpicStatus(epic);
        }
        subtasks.clear();
    }

    public void generateId(){
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
