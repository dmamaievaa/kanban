package manager;

import exceptions.ManagerSaveException;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager fileBackedTaskManager = Managers.getDefaultFileBackend();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            int maxId = 0;
            while ((line = bufferedReader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                if (!line.trim().isEmpty()) {
                    Task task = TaskConverter.fromString(line);
                    if (task.getId() > maxId) {
                        maxId = task.getId();
                    }
                    switch (task.getType()) {
                        case TASK -> fileBackedTaskManager.tasks.put(task.getId(), task);
                        case EPIC -> fileBackedTaskManager.epics.put(task.getId(), (Epic) task);
                        case SUBTASK -> {
                            fileBackedTaskManager.subtasks.put(task.getId(), (Subtask) task);
                            if (fileBackedTaskManager.epics.containsKey(task.getEpicId())) {
                                Epic epic = fileBackedTaskManager.epics.get(task.getEpicId());
                                epic.addSubtask(task.getId(), (Subtask) task);
                                fileBackedTaskManager.findEpicStatus(epic);
                            }
                        }
                    }
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Error occurred during reading the file", exception);
        }
        return fileBackedTaskManager;
    }

    public void save() {
        List<Task> tasks = new ArrayList<>();
        tasks.addAll(getAllTasks());
        tasks.addAll(getAllEpics());
        tasks.addAll(getAllSubtasks());
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append("id,type,name,status,description,epic" + "\n");
            for (Task task : tasks) {
                writer.write(TaskConverter.toString(task) + "\n");
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("File saving failed");
        }

    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task taskById = super.getTaskById(id);
        save();
        return taskById;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void removeTaskById(Integer id) {
        super.removeTaskById(id);
        save();
    }

    //Epic
    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeEpicById(Integer id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epicById = super.getEpicById(id);
        save();
        return epicById;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtaskById = super.getSubtaskById(id);
        save();
        return subtaskById;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeSubtaskById(Integer id) {
        super.removeSubtaskById(id);
        save();
    }


}
