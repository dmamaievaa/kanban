package manager;

import org.junit.jupiter.api.BeforeEach;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    @BeforeEach
    void preparation() {
        taskManager = Managers.getDefaultFileBackend();
    }
}


