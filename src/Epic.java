import java.util.ArrayList;
import java.util.Objects;

class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status);
        this.type = Type.EPIC;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void addSubtask(int id) {
        subtaskIds.add(id);
    }

    @Override
    public Type getType() {
        return Type.EPIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    @Override
    public String toString() {
        return Type.EPIC + " id:" + id + ", " +
                "status:" + status + ", " +
                "title:" + title + ", " +
                "description:" + description + "\n";
    }

}
