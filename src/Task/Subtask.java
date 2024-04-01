package Task;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, String title, String description, Status status, int epicId) {
        super(id, title, description, status);
        this.type = Type.SUBTASK;
        this.epicId = epicId;
    }

    public int getEpicId() {
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
