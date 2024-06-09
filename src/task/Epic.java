package task;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Epic extends Task {

    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
        this.type = Type.EPIC;
        this.status = Status.NEW;
    }

    public Epic(Integer id, String title, String description) {
        super(title, description);
        this.type = Type.EPIC;
        this.status = Status.NEW;
        this.id = id;
    }

    public Epic(Integer id, String title, String description, LocalDateTime startTime, long duration) {
        super(title, description);
        this.type = Type.EPIC;
        this.status = Status.NEW;
        this.id = id;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Integer id, Subtask subtask) {
        subtasks.put(id, subtask);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        this.type = Type.EPIC;
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public String toString() {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return Type.EPIC + " id:" + id + ", " +
                "status:" + status + ", " +
                "title:" + title + ", " +
                "description:" + description + ", " +
                "duration:" + hours + "h " + minutes + "m\n";
    }
}
