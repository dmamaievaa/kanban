package Manager;

import Task.Task;
import Task.Epic;
import Task.Subtask;
import Task.Status;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;

public class TaskManager {
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Subtask> subtasks;
    protected HashMap<Integer, Epic> epics;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    // Methods for tasks
    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Task with ID " + task.getId() + " not found");
        }
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
        epics.put(epic.getId(), epic);
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
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
        int id = subtask.getId();
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(id, subtask);
            findEpicStatus(epic);
        } else {
            System.out.println("Epic with ID " + subtask.getEpicId() + " not found.");
        }
    }

    public void updateSubtask(Subtask subtask) {
        int subtaskId = subtask.getId();
        System.out.println("subtaskId " + subtaskId);
        if (subtasks.containsKey(subtaskId)) {
            subtasks.put(subtaskId, subtask);
            Epic epicToUpdate = null;
            for (Epic epic : epics.values()) {
                if (epic.getSubtasks().containsValue(subtask)) {
                    epicToUpdate = epic;
                    epicToUpdate.getSubtasks().remove(subtaskId, subtask);
                    epicToUpdate.getSubtasks().put(subtaskId, subtask);
                    break;
                }
            }
            if (epicToUpdate != null) {
                findEpicStatus(epicToUpdate);
            }
        }
    }

    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.remove(id);
            for (Epic epic : epics.values()) {
                if (epic.getSubtasks().containsValue(subtask)) {
                    epic.getSubtasks().remove(subtask);
                    findEpicStatus(epic);
                    break;
                }
            }
        } else {
            System.out.println("Subtask with ID " + id + " not found.");
        }
    }

    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        return (subtask);
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

    public void printAllInstances() {
        System.out.println("Tasks:");
        System.out.println(getAllTasks());
        System.out.println("Subtasks");
        System.out.println(getAllSubtasks());
        System.out.println("Epics");
        System.out.println(getAllEpics());
    }
}
