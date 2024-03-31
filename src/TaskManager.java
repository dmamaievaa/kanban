import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;

public class TaskManager {
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Subtask> subtasks;
    protected HashMap<Integer, Epic> epics;
    protected static int id = 0;

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
            findEpicStatus(epic);
        } else {
            System.out.println("Epic with ID " + epic.getId() + " not found");
        }
    }

    public List<Subtask> getEpicSubtasks(Integer epicId) {
        List<Subtask> epicSubtasks = new ArrayList<>();
        for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {
            epicSubtasks.add(subtasks.get(subtaskId));
        }
        return epicSubtasks;
    }

    private void findEpicStatus(Epic epic) {
        List<Subtask> epicSubtasks = getEpicSubtasks(epic.getId());
        if (epicSubtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        int doneSubtasksCounter = 0;
        int newSubtasksCounter = 0;
        for (Subtask subtask : epicSubtasks) {
            if (subtask != null) {
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
            } else {
                System.out.println("Subtask with ID " + id + " not found.");
            }
        }

        if (doneSubtasksCounter == epicSubtasks.size()) {
            epic.setStatus(Status.DONE);
        } else if (newSubtasksCounter == epicSubtasks.size()) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public void removeEpicById(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = getEpicById(epicId);
            List<Integer> subtaskIdList = epic.getSubtaskIds();
            for (Integer subtaskId : subtaskIdList) {
                subtasks.remove(subtaskId);
            }
            epics.remove(epicId);
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

    // с HashMap я ловили эксепшны, прочла, что можно использовать HashSet
    public void removeAllEpics() {
        HashSet<Integer> epicIds = new HashSet<>(epics.keySet());
        for (int epicId : epicIds) {
            removeEpicById(epicId);
        }
    }

    // Methods for subtasks
    public void createSubtask(Subtask subtask) {
        subtask.setStatus(Status.NEW);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask.getId());
            findEpicStatus(epic);
        } else {
            System.out.println("Epic with ID " + subtask.getEpicId() + " not found.");
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId()) && epics.containsKey(subtask.getEpicId())) {
            Epic epicToUpdate = epics.get(subtask.getEpicId());
            subtasks.put(subtask.getId(), subtask);
            epicToUpdate.addSubtask(subtask.getId());
            findEpicStatus(epicToUpdate);
        } else {
            System.out.println("Subtask or epic with provided IDs doesn't exist");
        }
    }

    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            subtasks.remove(id);
            Epic epic = epics.get(subtask.getEpicId());
            findEpicStatus(epic);
        } else {
            System.out.println("Subtask with ID " + id + " doesn't exist");
        }
    }

    public Subtask getSubtaskById(int subtaskId) {
        return (Subtask) tasks.get(subtaskId);
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void removeAllSubtasks() {
        System.out.println("removeAllSubtasks");
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
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
