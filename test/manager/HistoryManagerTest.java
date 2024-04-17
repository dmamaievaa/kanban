package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HistoryManagerTest")
class HistoryManagerTest {

    protected Task task1;
    protected Epic epic1;
    protected Subtask subtask1InEpic1;
    protected Subtask subtask2InEpic1;
    protected HistoryManager historyManager;

    @BeforeEach
    void preparation() {
        historyManager = Managers.getDefaultHistory();
        task1 = new Task("Task1", "Task1 description", Status.NEW);
        epic1 = new Epic("Epic", "Epic description");
        subtask1InEpic1 = new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic1.getId());
        subtask2InEpic1 =   new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic1.getId());
    }

    @DisplayName("Add different types of tasks into history")
    @Test
    void add() {
        historyManager.add(task1);
        historyManager.add(epic1);
        historyManager.add(subtask1InEpic1);
        final List<Task> tasksHistory = historyManager.getHistory();
        assertNotNull(tasksHistory, "History is filled");
        assertEquals(3, tasksHistory.size(), "History is filled");
        assertTrue(tasksHistory.contains(task1), "Task is not reflected in history");
        assertTrue(tasksHistory.contains(epic1), "Epic is not reflected in history");
        assertTrue(tasksHistory.contains(subtask1InEpic1), "Subtask is not reflected in history");
    }
    @DisplayName("Show history after different manipulations")
    @Test
    void getHistory() {
        assertEquals(0, historyManager.getHistory().size(), "History is filled");
        List<Task> expectedHistory = new ArrayList<>();
        historyManager.add(task1);
        expectedHistory.add(task1);
        historyManager.add(epic1);
        expectedHistory.add(epic1);
        historyManager.add(subtask1InEpic1);
        expectedHistory.add(subtask1InEpic1);
        List<Task> actualHistory = historyManager.getHistory();
        assertNotNull(actualHistory, "History is empty");
        assertEquals(expectedHistory.size(), actualHistory.size(), "Sizes of history lists are different");
        assertTrue(expectedHistory.containsAll(actualHistory), "History lists are different");
    }


}