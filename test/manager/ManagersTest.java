package manager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {
    @DisplayName("Check that utility class Managers is providing initialized instances")
    @Test
    void shouldBeInitialized() {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(taskManager, "TaskManager is null");
        assertNotNull(historyManager, "HistoryManager is null");
    }

}