package task;

import java.util.HashMap;

public class Epic extends Task {

    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String title, String description) {
        super(title, description);
        this.type = Type.EPIC;
        this.status = Status.NEW;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
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
