package ru.yandex.practicum;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;

import ru.yandex.practicum.management.task.TaskManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    private static ByteArrayOutputStream output = new ByteArrayOutputStream();

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
        System.setOut(new PrintStream(output));
        manager.addSubtask(subtask1);
        assertEquals("Данный Subtask не подходит ни под один Epic", output.toString().trim());
        output = new ByteArrayOutputStream();


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
        System.setOut(new PrintStream(output));
        manager.deleteTaskById(2);
        assertEquals("Задач в списке нет", output.toString().trim());
        output = new ByteArrayOutputStream();

        addTasks();

        System.setOut(new PrintStream(output));
        manager.deleteTaskById(3);
        assertEquals("Задач с таким идентификатором не найдено", output.toString().trim());
        output = new ByteArrayOutputStream();

        manager.deleteTaskById(1);
        final int unexpectedSize = 2;
        assertNotEquals(unexpectedSize, manager.getTasks().size(), "Задача не удалена");

        manager.deleteAllTask();
        assertTrue(manager.getTasks().isEmpty(), "Не удалось удалить все Tasks");
    }

    @Test
    void deleteEpicsTest() {
        System.setOut(new PrintStream(output));
        manager.deleteEpicById(2);
        assertEquals("Задач в списке нет", output.toString().trim());
        output = new ByteArrayOutputStream();

        addSubtasksAndEpics();
        System.setOut(new PrintStream(output));
        manager.deleteEpicById(3);
        assertEquals("Задач с таким идентификатором не найдено", output.toString().trim());
        output = new ByteArrayOutputStream();

        manager.deleteEpicById(1);
        final int unexpectedSize = 2;
        assertNotEquals(unexpectedSize, manager.getTasks().size(), "Задача не удалена");

        manager.deleteAllEpic();
        assertTrue(manager.getEpics().isEmpty(), "Не удалось удалить все Epics");
        assertTrue(manager.getSubtasks().isEmpty(), "При удалении всех Epics не удалились Subtasks");
    }

    @Test
    void deleteSubtasksTest() {
        System.setOut(new PrintStream(output));
        manager.deleteSubtaskById(2);
        assertEquals("Задач в списке нет", output.toString().trim());
        output = new ByteArrayOutputStream();

        addSubtasksAndEpics();
        System.setOut(new PrintStream(output));
        manager.deleteSubtaskById(7);
        assertEquals("Задач с таким идентификатором не найдено", output.toString().trim());
        output = new ByteArrayOutputStream();

        manager.deleteSubtaskById(1);
        final int unexpectedSize = 3;
        assertNotEquals(unexpectedSize, manager.getTasks().size(), "Задача не удалена");

        manager.deleteAllSubtask();
        assertTrue(manager.getSubtasks().isEmpty(), "Не удалось удалить все Subtasks");

        final List<Integer> actualListSubtasks = manager.getEpics().get(1).getListIdSubtask();
        assertTrue(actualListSubtasks.isEmpty(),
                "При удалении всех Subtasks списки подзажач в  Epics не удалились");
    }

    @Test
    void getAllSubtaskOfEpicTest() {

        System.setOut(new PrintStream(output));
        manager.getAllSubtaskOfEpic(3);
        assertEquals("Задач с таким идентификатором не найдено", output.toString().trim());
        output = new ByteArrayOutputStream();

        addEpics();
        System.setOut(new PrintStream(output));
        manager.getAllSubtaskOfEpic(2);
        assertEquals("Подзадач в списке нет", output.toString().trim());
        output = new ByteArrayOutputStream();

        addSubtasksAndEpics();
        final List<Subtask> expectedList = List.of(subtask1, subtask2);
        List<Subtask> actualList = manager.getAllSubtaskOfEpic(1);
        assertEquals(expectedList, actualList, "Неверно выведен список Subtasks для Epic");
    }

    @Test
    void getHistoryWhenListIsEmpty() {
        manager.deleteAll();
        assertTrue(manager.getHistory().isEmpty(), "Список истории не пустой");
    }

    @Test
    void getHistoryWhenTaskViewRepeated() {
        completeBrowsingHistory();
        manager.getTaskById(1);
        final List<Task> expectedList = List.of(epic1, subtask1, task1);
        final List<Task> actualList = manager.getHistory();
        assertEquals(3, actualList.size(), "Неверно выведен список истории");
        assertEquals(expectedList, actualList, "Неверно выведен список истории");
    }

    @Test
    void getHistoryWhenTaskDeletedFromBeginning() {
        completeBrowsingHistory();
        manager.deleteTaskById(1);
        final List<Task> expectedList = List.of(epic1, subtask1);
        final List<Task> actualList = manager.getHistory();
        assertEquals(2, actualList.size(), "Неверно выведен список истории");
        assertEquals(expectedList, actualList, "Неверно выведен список истории");
    }

    @Test
    void getHistoryWhenTaskDeletedFromEnd() {
        completeBrowsingHistory();
        manager.deleteSubtaskById(5);
        final List<Task> expectedList = List.of(task1, epic1);
        final List<Task> actualList = manager.getHistory();
        assertEquals(2, actualList.size(), "Неверно выведен список истории");
        assertEquals(expectedList, actualList, "Неверно выведен список истории");
    }

    @Test
    void getHistoryWhenTaskDeletedFromMiddle() {
        completeBrowsingHistory();
        manager.deleteEpicById(3);
        final List<Task> expectedList = List.of(task1);
        final List<Task> actualList = manager.getHistory();
        assertEquals(expectedList, actualList, "Неверно выведен список истории");
    }

    @Test
    void getPrioritizedTasksTest() {
        addAllTasks();
        List<Task> list = List.of(task1, task2, epic1, epic2, subtask1, subtask2, subtask3);
        final TreeSet<Task> expectedSet = new TreeSet<>(list);
        final TreeSet<Task> actualSet = manager.getPrioritizedTasks();
        List<Task>  expected = new ArrayList<>(expectedSet);
        List<Task> actual = new ArrayList<>(actualSet);

        assertEquals(7, actualSet.size(), "Неверно выведен список задач по приоритету");
        assertEquals(expected, actual, "Неверно выведен список задач по приоритету");
    }


    protected void completeBrowsingHistory() {
        addAllTasks();
        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getSubtaskById(5);
    }

    @AfterAll
    static void afterAll() throws IOException {
        output.close();
    }

}
