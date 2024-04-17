package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final List<Task> tasksHistory;
    static final int MAX_VALUE = 10;
    public InMemoryHistoryManager() {
        this.tasksHistory = new ArrayList<>();
    }
    @Override
    public void add(Task task) {
        tasksHistory.add(task);
        if (tasksHistory.size() > MAX_VALUE) {
            tasksHistory.remove(0); // Удаляем самый старый элемент
        }
    }
    @Override
    public List<Task> getHistory() {
        return new ArrayList<Task>(tasksHistory);
    }

}
