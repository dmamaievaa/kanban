package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String title, String description, Status status, Integer epicId) {
        super(title, description, status);
        this.type = Type.SUBTASK;
        this.epicId = epicId;
    }

    public Subtask(Integer id, String title, String description, Status status, int epicId) {
        super(title, description);
        this.status = status;
        this.id = id;
        this.type = Type.SUBTASK;
        this.epicId = epicId;
    }

    public Subtask(String title, String description, Status status, int epicId, LocalDateTime startTime, Duration duration ) {
        super(title, description, status);
        this.type = Type.SUBTASK;
        this.epicId = epicId;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Subtask(Integer id, String title, String description, Status status, int epicId, LocalDateTime startTime, Duration duration ) {
        super(title, description, status);
        this.id = id;
        this.type = Type.SUBTASK;
        this.epicId = epicId;
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public Type getType() {
        return Type.SUBTASK;
    }

    @Override
    public String toString() {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return Type.SUBTASK + " subtask id:" + id + ", " +
                "assigned to epic with id:" + epicId + ", " +
                "status:" + status + ", " +
                "title:" + title + ", " +
                "description:" + description + ", " +
                "duration:" + hours + "h " + minutes + "m\n";
    }

}
