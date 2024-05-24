package manager;

import java.io.File;

public class Managers {
    static File file = new File("resources/task.csv");

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getDefaultFileBackend() {
        return new FileBackedTaskManager(file);
    }

}
