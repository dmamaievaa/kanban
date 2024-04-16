package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager{
    private final List<Task> tasksHistory;
    public InMemoryHistoryManager() {
        this.tasksHistory = new ArrayList<>();
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<Task>(tasksHistory);
    }

    @Override
    public void add(Task task) {
        if (!(tasksHistory.size() < 10)) {
            tasksHistory.removeFirst();
        }
        tasksHistory.add(task);
    }
}
