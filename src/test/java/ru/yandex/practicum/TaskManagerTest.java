package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;

import ru.yandex.practicum.exceptions.CollisionTaskException;
import ru.yandex.practicum.management.task.TaskManager;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {


    protected TaskManager manager;
    protected Task task1;
    protected Task task2;
    protected Epic epic1;
    protected Epic epic2;
    protected Subtask subtask1;
    protected Subtask subtask2;
    protected Subtask subtask3;



    protected void addTasks() {
         task1 = new Task("One", "Test  description", Status.NEW, 30, "10.03.2022 22:12");
         task2 = new Task("Two", "Test  description", Status.DONE, 30, "10.03.2022 23:12");
        manager.addTask(task1);
        manager.addTask(task2);
    }

    protected void addEpics() {
         epic1 = new Epic("One", "Test  description");
         epic2 = new Epic("Two", "Test  description");
        manager.addEpic(epic1);
        manager.addEpic(epic2);
    }

    protected void addSubtasksAndEpics() {
        addEpics();
        subtask1 = new Subtask("Qwerty", "qwerty", Status.NEW, 30, "12.02.2022 12:33", 1);
        subtask2 = new Subtask("qaz", "qwerty", Status.DONE, 30, "12.02.2022 14:33", 1);
        subtask3 = new Subtask("asd", "qwerty", Status.NEW, 30, "12.02.2022 15:33", 2);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
    }

    protected void addAllTasks() {
        addTasks();
        addEpics();
        subtask1 = new Subtask("Qwerty", "qwerty", Status.NEW, 30, "12.02.2022 12:33", 3);
        subtask2 = new Subtask("qaz", "qwerty", Status.DONE, 30, "12.02.2022 14:33", 3);
        subtask3 = new Subtask("asd", "qwerty", Status.NEW, 30, "12.02.2022 15:33", 4);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);


    }


    @Test
    void addAndGetTasksTestWhenTimeOfSubtasksIsNull() {
        addEpics();
        subtask1 = new Subtask("Qwerty", "qwerty", Status.NEW, 30, null, 1);
        manager.addSubtask(subtask1);
        assertEquals("Время ещё не задано", epic1.getEndTime());
        assertEquals("Время ещё не задано", epic1.getStartTime());

    }

    @Test
     void addAndGetTasksTest() {
        addTasks();
        final int taskId = manager.getId();
        final Task savedTask = manager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task2, savedTask, "Задачи не совпадают.");

        final Map<Integer, Task> tasks = manager.getTasks();
        assertNotEquals(new HashMap<>(), tasks, "Задачи не возвращаются.");
        int expectedSize = 2;
        assertEquals(expectedSize, tasks.size(), "Неверное количество задач.");
        assertEquals(task2, tasks.get(2), "Задачи не совпадают.");
    }

    @Test
     void addAndGetEpicsTest() {
        addEpics();
        final int epicId = manager.getId();
        final Task savedEpic = manager.getEpicById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic2, savedEpic, "Задачи не совпадают.");

        final Map<Integer, Epic> epics = manager.getEpics();
        assertNotEquals(new HashMap<>(), epics, "Задачи не возвращаются.");
        final int expectedSize = 2;
        assertEquals(expectedSize, epics.size(), "Неверное количество задач.");
        assertEquals(epic2, epics.get(2), "Задачи не совпадают.");
    }

    @Test
    void addAndGetSubtasksTest() {

        subtask1 = new Subtask("OneSubtask", "Test  description",Status.NEW, 30, "12.02.2022 12:33", 1);
        manager.addSubtask(subtask1);
        assertEquals(0, manager.getSubtasks().size(), "Задача не добавилась, Epic еще нет");

        addSubtasksAndEpics();
        final int subtaskId = manager.getId();
        final Subtask savedSubtask = manager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask3, savedSubtask, "Задачи не совпадают.");

        final Map<Integer, Subtask> subtasks = manager.getSubtasks();
        assertNotEquals(new HashMap<>(), subtasks, "Задачи не возвращаются.");
        final int expectedSize = 3;
        assertEquals(expectedSize, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask2, subtasks.get(4), "Задачи не совпадают.");
    }

    @Test
    void deleteTasksTest() {
        int unexpectedSize = 0;

        manager.deleteTaskById(2);
        assertEquals(unexpectedSize, manager.getTasks().size(), "Список задач должен быть пустым");

        addTasks();
        unexpectedSize = 2;
        manager.deleteTaskById(3);
        assertEquals(unexpectedSize, manager.getTasks().size(),
                "Список задач не должен измениться, задачи с таким id не в списке");


        manager.deleteTaskById(1);
        assertNotEquals(unexpectedSize, manager.getTasks().size(), "Задача не удалена");

        manager.deleteAllTask();
        assertTrue(manager.getTasks().isEmpty(), "Не удалось удалить все Tasks");
    }

    @Test
    void deleteEpicsTest() {
        int unexpectedSize = 0;
        manager.deleteEpicById(2);
        assertEquals(unexpectedSize, manager.getEpics().size(), "Список задач должен быть пустым");

        addSubtasksAndEpics();
        unexpectedSize = 2;
        manager.deleteEpicById(3);
        assertEquals(unexpectedSize, manager.getEpics().size(),
                "Список задач не должен измениться, задачи с таким id не в списке");

        manager.deleteEpicById(1);
        unexpectedSize = 1;
        assertNotEquals(unexpectedSize, manager.getTasks().size(), "Задача не удалена");

        manager.deleteAllEpic();
        assertTrue(manager.getEpics().isEmpty(), "Не удалось удалить все Epics");
        assertTrue(manager.getSubtasks().isEmpty(), "При удалении всех Epics не удалились Subtasks");
    }

    @Test
    void deleteSubtasksTest() {
        int unexpectedSize = 0;
        manager.deleteSubtaskById(2);
        assertEquals(unexpectedSize, manager.getSubtasks().size(), "Список задач должен быть пустым");

        addSubtasksAndEpics();
        manager.deleteSubtaskById(7);
        unexpectedSize = 3;
        assertEquals(unexpectedSize, manager.getSubtasks().size(),
                "Список задач не должен измениться, задачи с таким id не в списке");


        manager.deleteSubtaskById(1);
        assertNotEquals(unexpectedSize, manager.getTasks().size(), "Задача не удалена");

        manager.deleteAllSubtask();
        assertTrue(manager.getSubtasks().isEmpty(), "Не удалось удалить все Subtasks");

        final List<Integer> actualListSubtasks = manager.getEpics().get(1).getListIdSubtask();
        assertTrue(actualListSubtasks.isEmpty(),
                "При удалении всех Subtasks списки подзажач в  Epics не удалились");
    }

    @Test
    void getAllSubtaskOfEpicTest() {
        int unexpectedSize = 0;
        manager.getAllSubtaskOfEpic(3);
        assertEquals(unexpectedSize, manager.getSubtasks().size(), "Список задач должен быть пустым, Epics нет");

        addEpics();
        manager.getAllSubtaskOfEpic(2);
        assertEquals(unexpectedSize, manager.getSubtasks().size(),
                "Список задач должен быть пустым, Subtasks еще не добавлены");

        addSubtasksAndEpics();
        final List<Subtask> expectedList = List.of(subtask1, subtask2);
        List<Subtask> actualList = manager.getAllSubtaskOfEpic(1);
        assertEquals(expectedList, actualList, "Неверно выведен список Subtasks для Epic");
    }


    @Test
    void getPrioritizedTasksTest() {
        addAllTasks();
        List<Task> list = List.of(task1, task2, subtask1, subtask2, subtask3);
        final TreeSet<Task> expectedSet = new TreeSet<>(list);
        final Set<Task> actualSet = manager.getPrioritizedTasks();
        List<Task>  expected = new ArrayList<>(expectedSet);
        List<Task> actual = new ArrayList<>(actualSet);

        assertEquals(5, actualSet.size(), "Неверно выведен список задач по приоритету");
        assertEquals(expected, actual, "Неверно выведен список задач по приоритету");
    }

    @Test
    void shouldThrowExceptionWhenStartCollidesWithEndTimeOfTasksForAdd() {
        addAllTasks();
        Task newTask = new Task("Two", "Test  description", Status.NEW, 30, "10.03.2022 22:33");
        Subtask newSubtask = new Subtask("Qwerty", "qwerty", Status.NEW, 30, "12.02.2022 15:34", 3);

        assertThrows(CollisionTaskException.class, () -> manager.addTask(newTask),
                "Не выявлен случай: задача начинается до окончания другой задачи");
        assertThrows(CollisionTaskException.class, () -> manager.addSubtask(newSubtask),
                "Не выявлен случай: задача начинается до окончания другой задачи");
    }

    @Test
    void shouldThrowExceptionWhenEndCollidesWithStartTimeOfTasksForAdd() {
        addAllTasks();
        Task newTask = new Task("Two", "Test  description", Status.NEW, 30, "10.03.2022 22:00");
        Subtask newSubtask = new Subtask("Qwerty", "qwerty", Status.NEW, 30, "12.02.2022 12:23", 3);

        assertThrows(CollisionTaskException.class, () -> manager.addTask(newTask),
                "Не выявлен случай: задача заканчивается после начала другой задачи");
        assertThrows(CollisionTaskException.class, () -> manager.addSubtask(newSubtask),
                "Не выявлен случай:  задача заканчивается после начала другой задачи");
    }

    @Test
    void shouldThrowExceptionWhenInternalCollidesTimeOfTasksForAdd() {
        addAllTasks();
        Task newTask = new Task("Two", "Test  description", Status.NEW, 10, "10.03.2022 22:15");
        Subtask newSubtask = new Subtask("Qwerty", "qwerty", Status.NEW, 10, "12.02.2022 15:35", 3);

        assertThrows(CollisionTaskException.class, () -> manager.addTask(newTask),
                "Не выявлен случай: время задачи находится внутри времени другой задачи");
        assertThrows(CollisionTaskException.class, () -> manager.addSubtask(newSubtask),
                "Не выявлен случай: время задачи находится внутри времени другой задачи");

    }

    @Test
    void shouldThrowExceptionWhenExternalCollidesTimeOfTasksForAdd() {
        addAllTasks();
        Task newTask = new Task("Two", "Test  description", Status.NEW, 50, "10.03.2022 22:10");
        Subtask newSubtask = new Subtask("Qwerty", "qwerty", Status.NEW, 50, "12.02.2022 15:22", 3);

        assertThrows(CollisionTaskException.class, () -> manager.addTask(newTask),
                "Не выявлен случай: время другой задачи находися внутри времени добавленной задачи");
        assertThrows(CollisionTaskException.class, () -> manager.addSubtask(newSubtask),
                "Не выявлен случай: время другой задачи находися внутри времени добавленной задачи");

    }

    @Test
    void shouldThrowExceptionWhenStartCollidesWithEndTimeOfTasksForUpdate() {
        addAllTasks();
        Task newTask = new Task("Two", "Test  description", Status.NEW, 30, "10.03.2022 22:33");
        Subtask newSubtask = new Subtask("Qwerty", "qwerty", Status.NEW, 30, "12.02.2022 14:35", 3);
        newTask.setId(2);
        newSubtask.setId(5);

        assertThrows(CollisionTaskException.class, () -> manager.updateTask(newTask),
                "Не выявлен случай: задача начинается до окончания другой задачи");
        assertThrows(CollisionTaskException.class, () -> manager.updateTask(newSubtask),
                "Не выявлен случай: задача начинается до окончания другой задачи");
    }

    @Test
    void shouldThrowExceptionWhenEndCollidesWithStartTimeOfTasksUpdate() {
        addAllTasks();
        Task newTask = new Task("Two", "Test  description", Status.NEW, 30, "10.03.2022 22:15");
        Subtask newSubtask = new Subtask("Qwerty", "qwerty", Status.NEW, 30, "12.02.2022 14:22", 3);
        newTask.setId(2);
        newSubtask.setId(5);

        assertThrows(CollisionTaskException.class, () -> manager.updateTask(newTask),
                "Не выявлен случай: задача заканчивается после начала другой задачи");
        assertThrows(CollisionTaskException.class, () -> manager.updateTask(newSubtask),
                "Не выявлен случай: задача заканчивается после начала другой задачи");
    }

    @Test
    void shouldThrowExceptionWhenInternalCollidesTimeOfTasksForUpdate() {
        addAllTasks();
        Task newTask = new Task("Two", "Test  description", Status.NEW, 10, "10.03.2022 22:15");
        Subtask newSubtask = new Subtask("Qwerty", "qwerty", Status.NEW, 10, "12.02.2022 14:35", 3);
        newTask.setId(2);
        newSubtask.setId(5);

        assertThrows(CollisionTaskException.class, () -> manager.updateTask(newTask),
                "Не выявлен случай: время задачи находится внутри времени другой задачи");
        assertThrows(CollisionTaskException.class, () -> manager.updateTask(newSubtask),
                "Не выявлен случай: время задачи находится внутри времени другой задачи");
    }

    @Test
    void shouldThrowExceptionWhenExternalCollidesTimeOfTasksForUpdate() {
        addAllTasks();
        Task newTask = new Task("Two", "Test  description", Status.NEW, 50, "10.03.2022 22:10");
        Subtask newSubtask = new Subtask("Qwerty", "qwerty", Status.NEW, 50, "12.02.2022 14:22", 3);
        newTask.setId(2);
        newSubtask.setId(5);

        assertThrows(CollisionTaskException.class, () -> manager.updateTask(newTask),
                "Не выявлен случай: время другой задачи находися внутри времени добавленной задачи");
        assertThrows(CollisionTaskException.class, () -> manager.updateTask(newSubtask),
                "Не выявлен случай: время другой задачи находися внутри времени добавленной задачи");
    }

    @Test
    void shouldChangeListOfPrioritizedTasksWhenUpdateTask() {
        addAllTasks();
        Task newTask = new Task("Two", "Test  description", Status.NEW, 50, "11.03.2022 22:10");
        Subtask newSubtask = new Subtask("Qwerty", "qwerty", Status.NEW, 50, "13.02.2022 14:22", 3);
        newTask.setId(2);
        newSubtask.setId(5);
        manager.updateTask(newTask);
        manager.updateTask(newSubtask);

        System.out.println(manager.getPrioritizedTasks());

        assertEquals(5, manager.getPrioritizedTasks().size(),
                "При обновлении задач список приоритетов обновился неверно");
    }

    @Test
    void shouldIncreaseListOfPrioritizedTasksWhenAddTask() {
        addAllTasks();
        Task newTask = new Task("Two", "Test  description", Status.NEW, 50, "11.03.2022 22:10");
        Subtask newSubtask = new Subtask("Qwerty", "qwerty", Status.NEW, 50, "13.02.2022 14:22", 3);
        manager.addSubtask(newSubtask);
        manager.addTask(newTask);

        assertEquals(7, manager.getPrioritizedTasks().size(),
                "При обновлении задач список приоритетов обновился неверно");
    }
}
