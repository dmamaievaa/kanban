# Техническое задание проекта №4
## Трекер задач
Бэкенд для трекера задач. 
## Типы задач
В системе задачи могут быть трёх типов: обычные задачи, эпики и подзадачи. 
 1. Простая задача (Task.Task)
 2. Эпик (Task.Task.Epic) -  большая задача, которая делится на подзадачи
 3. Подзадача (Task.Subtask)
## Свойства задач
1. Название, кратко описывающее суть задачи (например, «Переезд»).
2. Описание, в котором раскрываются детали.
3. Уникальный идентификационный номер задачи, по которому её можно будет найти.
4. Статус, отображающий её прогресс: 
* NEW — задача только создана, но к её выполнению ещё не приступили. 
* IN_PROGRESS — над задачей ведётся работа. 
* DONE — задача выполнена.
### Специфика эпиков и подзадач:
* Для каждой подзадачи известно, в рамках какого эпика она выполняется.
* Каждый эпик знает, какие подзадачи в него входят.
* Завершение всех подзадач эпика считается завершением эпика.

Полный текст задания: [Техническое задание проекта №4](https://practicum.yandex.ru/learn/java-developer/courses/f3f8cbf3-865d-4b16-9339-b55ac641633f/sprints/234034/topics/cb5e9f5a-1da9-4629-a038-c6f8b4d049db/lessons/f7e719dd-ded6-4181-bf88-31f0b5842c29/)

   


