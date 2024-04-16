package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static task.Status.NEW;

class HistoryManagerTest {

    protected Task task1;
    protected Epic epic1;
    protected Subtask subtask1InEpic1;
    protected Subtask subtask2InEpic1;

    protected HistoryManager historyManager;

    @BeforeEach
    void fillHistory() {
        historyManager = Managers.getDefaultHistory();
        task1 = new Task("Task1", "Task1 description", Status.NEW);
        epic1 = new Epic("Epic", "Epic description");
        subtask1InEpic1 = new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic1.getId());
        subtask2InEpic1 =   new Subtask("First subtask in epic 1",
                "First subtask description", Status.NEW, epic1.getId());
    }

    @Test
    void add() {
        historyManager.add(epic1);
        historyManager.add(task1);
        historyManager.add(subtask1InEpic1);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "History is filled");
        assertEquals(3, history.size(), "History is filled");
        assertTrue(history.contains(task1), "Task is not reflected in history");
        assertTrue(history.contains(epic1), "Epic is not reflected in history");
        assertTrue(history.contains(subtask1InEpic1), "Subtask is not reflected in history");

        historyManager.add(task1);
        final List<Task> historyUpdated = historyManager.getHistory();
        assertTrue(historyUpdated.contains(task1), "Задача не попала в историю");
        assertEquals(task1, historyUpdated.get(historyUpdated.size() - 1), "Новая добавленная задача не попала в конец списка истории");
        int count = 0;
        for (Task task : historyUpdated) {
            if (task.equals(task1)) {
                count += 1;
            }
        }
        //assertEquals(1, count, "В истории более одного экземпляра задачи");
    }

    @Test
    void getHistory() {
        assertEquals(0, historyManager.getHistory().size(), "History is filled");
        List<Task> expectedListHistory = new ArrayList<>();
        historyManager.add(task1);
        expectedListHistory.add(task1);
        historyManager.add(epic1);
        expectedListHistory.add(epic1);
        historyManager.add(subtask1InEpic1);
        expectedListHistory.add(subtask1InEpic1);
        List<Task> listHistory = historyManager.getHistory();
        assertNotNull(listHistory, "History is empty");
        assertEquals(expectedListHistory.size(), listHistory.size(), "Sizes of history lists are different");
        assertTrue(expectedListHistory.containsAll(listHistory), "History lists are different");
    }


}