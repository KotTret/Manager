package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.management.task.Managers;
import ru.yandex.practicum.management.task.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HistoryManagerTest {
    protected TaskManager manager;
    protected Task task1;
    protected Task task2;
    protected Epic epic1;
    protected Epic epic2;
    protected Subtask subtask1;
    protected Subtask subtask2;
    protected Subtask subtask3;

    @BeforeEach
    void beforeEach() {
        manager = Managers.getInMemoryTaskManager();
    }

    protected void  addAllTasks() {
        task1 = new Task("One", "Test  description", Status.NEW, 30, "10.03.2022 22:12");
        task2 = new Task("Two", "Test  description", Status.DONE, 30, "10.03.2022 23:12");
        epic1 = new Epic("One", "Test  description");
        epic2 = new Epic("Two", "Test  description");
        subtask1 = new Subtask("Qwerty", "qwerty", Status.NEW, 30, "12.02.2022 12:33", 3);
        subtask2 = new Subtask("qaz", "qwerty", Status.DONE, 30, "12.02.2022 14:33", 3);
        subtask3 = new Subtask("asd", "qwerty", Status.NEW, 30, "12.02.2022 15:33", 4);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
    }

    protected void completeBrowsingHistory() {
        addAllTasks();
        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getSubtaskById(5);
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
}
