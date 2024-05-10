package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {
    // Methods for tasks
    List<Task> getHistory();

    void createTask(Task task);

    void updateTask(Task task);

    void removeTaskById(int taskId);

    Task getTaskById(int taskId);

    List<Task> getAllTasks();

    void removeAllTasks();

    // Methods for epics
    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    void findEpicStatus(Epic epic);

    void removeEpicById(int epicId);

    Epic getEpicById(int epicId);

    List<Epic> getAllEpics();

    void removeAllEpics();

    // Methods for subtasks
    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void removeSubtaskById(int id);

    Subtask getSubtaskById(int subtaskId);

    List<Subtask> getAllSubtasks();

    void removeAllSubtasks();

}
