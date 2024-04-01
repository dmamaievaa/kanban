package Task;

import java.util.HashMap;

public class Epic extends Task {

    private HashMap<Integer, Subtask> subtasks = new HashMap<>();;

    public Epic(int id, String title, String description) {
        super(id, title, description);
        this.type = Type.EPIC;
        this.status = Status.NEW;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }
    public void setSubtasks(HashMap<Integer, Subtask> subtasks) {
        this.subtasks = subtasks;
    }
    public void addSubtask(int id, Subtask subtask) {
        subtasks.put(id, subtask);
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public String toString() {
        return Type.EPIC + " id:" + id + ", " +
                "status:" + status + ", " +
                "title:" + title + ", " +
                "description:" + description + "\n";
    }


}
