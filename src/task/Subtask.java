package task;

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
        return Type.SUBTASK + " subtask id:" + id + ", " +
                "assigned to epic with id:" + epicId + ", " +
                "status:" + status + ", " +
                "title:" + title + ", " +
                "description:" + description + "\n";
    }

}
