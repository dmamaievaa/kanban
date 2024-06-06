package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected Integer id;
    protected String title;
    protected String description;
    protected Status status;
    protected Type type;
    protected Duration duration;
    protected LocalDateTime startTime;


    public Task(Integer id, String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
        this.id = id;
    }

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.type = Type.TASK;
    }

    public Task(String title, String description, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.type = Type.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(Integer id, String title, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = Type.TASK;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return Type.TASK;
    }

    public Integer getEpicId() {
        return null;
    }

    public LocalDateTime getEndTime(){
        if (!(startTime == null) && !duration.isZero()){
            return startTime.plus(duration);
        }
        return null;
    }

    public Duration getDuration(){
        return duration;
    }

    public void setDuration(Duration duration){
        this.duration=duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return Type.TASK + " id:" + id + ", " +
                "status:" + status + ", " +
                "title:" + title + ", " +
                "description:" + description + ", " +
                "duration:" + hours + "h " + minutes + "m\n";
    }

}
