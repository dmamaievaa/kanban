package manager;

import task.*;

public class TaskConverter {
    protected static String toString(Task task) {
        return task.getId() + "," + task.getTitle() + "," + task.getStatus() + "," + task.getDescription() +
                "," + task.getEpicId();

    }

    protected static Task fromString(String value) {
        String[] columns = value.split(",");
        int id = Integer.parseInt(columns[0]);
        Type type = Type.valueOf(columns[1]);
        String title = columns[2];
        Status status = Status.valueOf(columns[3]);
        String description = columns[4];
        Task task;
        switch (type) {
            case TASK -> task = new Task(id, title, description, status);
            case EPIC -> task = new Epic(id, title, description);
            case SUBTASK -> {
                int epicId = Integer.parseInt(columns[5]);
                task = new Subtask(id, title, description, status, epicId);
            }
            default -> throw new IllegalArgumentException("Unknown task type: " + type);
        }
        return task;
    }

}
