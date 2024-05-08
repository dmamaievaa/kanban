package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@DisplayName("HistoryManagerTest")
class HistoryManagerTest {

    protected Task task1;
    protected Task task2;
    protected Task task3;
    protected Epic epic1;
    protected Subtask subtask1InEpic1;
    protected Subtask subtask2InEpic1;
    protected HistoryManager historyManager;

    @BeforeEach
    void preparation() {
        historyManager = Managers.getDefaultHistory();
        task1 = new Task(1, "Task1", "Task1 description", Status.NEW);
        task2 = new Task(2, "Task2", "Task2 description", Status.NEW);
        task3 = new Task(3, "Task3", "Task3 description", Status.NEW);

        epic1 = new Epic(4, "Epic", "Epic description");
        subtask1InEpic1 = new Subtask(5, "First subtask in epic 1",
                "First subtask description", Status.NEW, epic1.getId());
        subtask2InEpic1 = new Subtask(6, "First subtask in epic 1",
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
        System.out.println(expectedHistory);
        System.out.println(historyManager.getHistory());
        List<Task> actualHistory = historyManager.getHistory();
        assertNotNull(actualHistory, "History is empty");
        assertEquals(expectedHistory.size(), actualHistory.size(), "Sizes of history lists are different");
        assertTrue(expectedHistory.containsAll(actualHistory), "History lists are different");
    }

    @DisplayName("Remove first task")
    @Test
    public void shouldRemoveFirst() {
        List<Task> expectedHistory = new ArrayList<>();
        historyManager.add(task1);
        historyManager.add(task2);
        expectedHistory.add(task2);
        historyManager.add(task3);
        expectedHistory.add(task3);
        historyManager.remove(1);
        assertIterableEquals(expectedHistory, historyManager.getHistory(), "First task in the list wasn't removed");
    }

    @DisplayName("Remove task from the middle")
    @Test
    public void shouldRemoveTaskFromTheMiddle() {
        List<Task> expectedHistory = new ArrayList<>();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        expectedHistory.add(task1);
        expectedHistory.add(task3);
        historyManager.remove(2);
        assertIterableEquals(expectedHistory, historyManager.getHistory(), "Task from the middle of the list wasn't removed");
    }

    @DisplayName("Remove last task")
    @Test
    public void shouldRemoveLastTask() {
        List<Task> expectedHistory = new ArrayList<>();
        historyManager.add(task1);
        expectedHistory.add(task1);
        historyManager.add(task2);
        expectedHistory.add(task2);
        historyManager.add(task3);
        historyManager.remove(3);
        assertIterableEquals(expectedHistory, historyManager.getHistory(), "Last task in the list wasn't removed");
    }

    @DisplayName("Remove when there is only 1 task")
    @Test
    public void shouldRemoveLoneTask() {
        List<Task> expectedHistory = new ArrayList<>();
        historyManager.add(task1);
        expectedHistory.add(task1);
        historyManager.remove(1);
        assertEquals(Collections.emptyList(), historyManager.getHistory(), "Lone task still presents in history");
    }


}