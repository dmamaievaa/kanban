package manager;

import task.Task;

import java.util.List;

public interface HistoryManager {
    <T extends Task> void add(T task);

    List<Task> getHistory();

    void remove(int id);
}
