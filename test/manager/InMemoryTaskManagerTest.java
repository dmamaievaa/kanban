package manager;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
   @BeforeEach
    void preparation() {
       taskManager = new InMemoryTaskManager();
    }
}