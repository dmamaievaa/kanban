package manager;
import task.Task;
import java.util.List;
//Интерфейс для управления историей просмотров
public interface HistoryManager {
    void add(Task task);
    List <Task> getHistory();
}
