
public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        // создаем задачи
        Task task1 = new Task(1, "First task", "First task description", Status.NEW);
        Task task2 = new Task(2, "Second task", "Second task description", Status.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        //создаем эпики и сабтаски к ним
        Epic epic1 = new Epic(1, "First epic", "First epic description", Status.NEW);
        Epic epic2 = new Epic(2, "Second epic", "Second epic description", Status.NEW);
        Subtask subtask1InEpic1 = new Subtask(1, "First subtask in epic 1",
                "First subtask description", Status.NEW, 1);
        Subtask subtask2InEpic1 = new Subtask(2, "Second subtask in epic 1",
                "Second subtask description", Status.NEW, 1);
        Subtask subtaskInEpic2 = new Subtask(3, "Subtask in epic 2",
                "Subtask description", Status.NEW, 2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1InEpic1);
        taskManager.createSubtask(subtask2InEpic1);
        taskManager.createSubtask(subtaskInEpic2);
        //печать всех инстансов через переопределенный toString()
        taskManager.printAllInstances();
       /* Меняем статусы через обновление объектов -->
        Фраза «информация приходит вместе с информацией по задаче» означает, что не существует отдельного метода,
        который занимался бы только обновлением статуса задачи. Вместо этого статус задачи обновляется вместе с
        полным обновлением задачи.*/
        Task task1Updated = new Task(1, "First task after update", "First task with new status",
                Status.IN_PROGRESS);
        taskManager.updateTask(task1Updated);
        Subtask subtaskInEpic2Updated = new Subtask(3, "Subtask in epic 2 after update",
                "Subtask was finished", Status.DONE, 2);
        taskManager.updateSubtask(subtaskInEpic2Updated);
        taskManager.printAllInstances();
        //Удаляем задачу и 1 из эпиков со всеми сабтасками
        taskManager.removeTaskById(1);
        taskManager.removeEpicById(1);
        taskManager.printAllInstances();


       /* тест переопределенных equals() и hashCode()
        Epic epicToTest1 = new Epic(111, "Epic to test", "Epic to test", Status.NEW);
        Epic epicToTest2 = new Epic(111, "Epic to test", "Epic to test", Status.NEW);
        epicToTest1.addSubtask(1);
        epicToTest2.addSubtask(1);
        System.out.println("epic1 equals epic2: " + epicToTest1.equals(epicToTest2));
        System.out.println(epicToTest1.hashCode());
        System.out.println(epicToTest2.hashCode());*/

    }
}
