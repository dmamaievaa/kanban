package manager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;
import java.util.ArrayList;
import java.util.List;


//abstract class TaskManagerTest <T extends TaskManager>  {
    class TaskManagerTest{
  //  protected TaskManager taskManager;
    protected TaskManager taskManager = new InMemoryTaskManager();

    protected Task task1;
    protected Epic epic1;
    protected Subtask subtask1Epic1;
    protected Subtask subtask2Epic1;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @BeforeEach
    void prepareData() {
        task1 = new Task("Покупки", "Список покупок",  Status.NEW);
        taskManager.createTask(task1);
        epic1 = new Epic("Большая задача1", "Нужно было описать");
        taskManager.createEpic(epic1);
        subtask1Epic1 = new Subtask("Подзадача1эпик1", "у меня нет фантазии", Status.NEW, 2);
        subtask2Epic1 = new Subtask("Подзадача2эпик1", "у меня нет фантазии совсем",  Status.NEW, 2);
        taskManager.createSubtask(subtask1Epic1);
        taskManager.createSubtask(subtask2Epic1);
    }

    @Test
    void getHistory() {
    }

    @Test
    void createTask() {
        Task task3 = new Task("Тестовая задача", "Описание тестовой задачи",  Status.NEW);
        taskManager.createTask(task3);
        Task savedTask = taskManager.getTaskById(task3.getId());
        assertNotNull(savedTask, "Задача не найдена");
        assertEquals(task3, savedTask, "Задачи не совпадают");
        final List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertTrue(tasks.contains(task3), "Задачи нет в списке задач");
    }




        @Test
    void updateTask() {
        task1.setStatus(Status.IN_PROGRESS);
        task1.setDescription("Изменение описание задачи");
        taskManager.updateTask(task1);
        assertNotNull(task1.getId(), "Некорректный id");
        Task expectedUpdatedTask = new Task(1, "Покупки", "Изменение описание задачи", Status.IN_PROGRESS);
        assertEquals(expectedUpdatedTask, taskManager.getTaskById(1), "Обновление задачи не произошло");
        assertTrue(taskManager.getAllTasks().contains(expectedUpdatedTask), "Обновление задачи в списке не произошло");
    }

    @Test
    void removeTaskById() {
        taskManager.removeTaskById(task1.getId());
       // assertNotNull(task1.getId(), "Некорректный id");
        assertFalse(taskManager.getAllTasks().contains(task1), "Задача не удалена");
        assertFalse(taskManager.getHistory().contains(task1), "Задача не удалена из истории");
    }


    @Test
    void getTaskById() {
       // Task task1 = new Task("Покупки", "Список покупок",  Status.NEW);
        Task gotTask = taskManager.getTaskById(task1.getId());
       // assertNotNull(gotTask.getId(), "Некорректный id");
        assertEquals(task1, gotTask, "Получена не та задача");
        assertTrue(taskManager.getHistory().contains(gotTask), "Задача не попала в историю");
    }

    @Test
    void getAllTasks() {
        //Task task1 = new Task("Покупки", "Список покупок",  Status.NEW);
        List<Task> expectedListTask = new ArrayList<>();
        expectedListTask.add(task1);
        List<Task> listAllTask = taskManager.getAllTasks();
        assertEquals(expectedListTask.size(), listAllTask.size(), "Размеры списков задач не равны");
        assertTrue(expectedListTask.containsAll(listAllTask), "Списки не совпадают");
    }

    @Test
    void removeAllTasks() {
        //Task task1 = new Task("Покупки", "Список покупок",  Status.NEW);
        taskManager.getTaskById(task1.getId());
        taskManager.removeAllTasks();
        assertEquals(0, taskManager.getAllTasks().size(), "Список задач не пуст");
        //проверяем историю
        assertTrue(taskManager.getHistory().isEmpty(), "История не пуста");
    }

    @Test
    void createEpic() {
        Epic epic2 = new Epic("Большая задача2", "Просто нужно что-то написать");
        taskManager.createEpic(epic2);
        Task savedEpic = taskManager.getEpicById(epic2.getId());
        assertNotNull(savedEpic, "Эпик не найден");
        assertEquals(epic2, savedEpic, "Эпики не совпадают");
        final List<Epic> epicList = taskManager.getAllEpics();
        assertNotNull(epicList, "Эпик не возвращается");
        assertEquals(2, epicList.size(), "Неверное количество эпиков");
        assertTrue(epicList.contains(epic2), "Эпика нет в списке эпиков");
    }

    @Test
    void updateEpic() {
        epic1.setDescription("Изменение описание эпика");
        epic1.setTitle("Изменение название эпика");
        taskManager.updateEpic(epic1);
        taskManager.removeAllSubtasks();
        Epic expectedUpdatedEpic = new Epic("afaf", "Изменение название эпика");
        //assertNotNull(epic1.getId(), "Некорректный id");
        assertEquals(expectedUpdatedEpic, epic1, "Обновление задачи не произошло");
        assertTrue(taskManager.getAllEpics().contains(expectedUpdatedEpic), "Обновление задачи в списке не произошло");
    }



    @Test
    void removeEpicById() {
        taskManager.removeEpicById(epic1.getId());
        assertFalse(taskManager.getAllEpics().contains(epic1), "Эпик не удален");
        assertFalse(taskManager.getAllSubtasks().contains(subtask1Epic1), "При удалении эпика подзадача эпика не удалилась из хранилища");
        assertFalse(taskManager.getHistory().contains(epic1), "Эпик не удален из истории");
        assertNotNull(epic1.getId(), "Некорректный id");
    }

    @Test
    void getEpicById() {
        Epic gotEpic = taskManager.getEpicById(epic1.getId());
        assertNotNull(gotEpic, "Эпик не найден");
        assertNotNull(gotEpic.getId(), "Некорректный id");
        assertEquals(epic1, gotEpic, "Получен не тот эпик");
        assertTrue(taskManager.getHistory().contains(gotEpic), "Эпик не сохранился в истории");
    }

    @Test
    void getAllEpics() {
        List<Epic> expectedListEpics = new ArrayList<>();
        expectedListEpics.add(epic1);
        List<Epic> listAllEpic = taskManager.getAllEpics();
        assertEquals(expectedListEpics.size(), listAllEpic.size(), "Размеры списков эпиков не равны");
        assertTrue(expectedListEpics.containsAll(listAllEpic), "Списки не совпадают");
    }

    @Test
    void removeAllEpics() {
        taskManager.getEpicById(epic1.getId()); //заполнили историю
        taskManager.removeAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "Хранилие эпиков не очистилось");
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Хранилие подзадач не очистилось");
        //проверка истории
        assertFalse(taskManager.getHistory().contains(epic1), "История не пуста");
    }

    @Test
    void createSubtask() {
        Subtask subtask3Epic1 = new Subtask("Подзадача3эпик1", "Ох уж эти тесты(", Status.NEW, 1);
        taskManager.createSubtask(subtask3Epic1);
        Task savedSubtask = taskManager.getSubtaskById(subtask3Epic1.getId());
        assertNotNull(savedSubtask, "Подзадача не найдена");
        assertEquals(subtask3Epic1, savedSubtask, "Подзадачи не совпадают");
        final List<Subtask> subtasksList = taskManager.getAllSubtasks();
        assertNotNull(subtasksList, "Подзадача не возвращается");
        assertEquals(3, subtasksList.size(), "Неверное количество Подзадач");
        assertTrue(subtasksList.contains(subtask3Epic1), "Подзадачи нет в списке подзадач");
        assertEquals(epic1.getId(), subtask3Epic1.getEpicId(), "Подзадача не соответствует Эпику");
        assertEquals(Status.NEW, epic1.getStatus(), "Статус эпика изменился, а не должен был");
    }

    @Test
    void updateSubtask() {

    }

    @Test
    void removeSubtaskById() {
    }

    @Test
    void getSubtaskById() {
    }

    @Test
    void getAllSubtasks() {
    }

    @Test
    void removeAllSubtasks() {
    }
}
