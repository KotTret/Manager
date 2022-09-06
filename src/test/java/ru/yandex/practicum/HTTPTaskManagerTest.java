package ru.yandex.practicum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.management.task.HTTPTaskManager;
import ru.yandex.practicum.management.task.Managers;
import ru.yandex.practicum.management.task.TaskManager;
import ru.yandex.practicum.servers.KVServer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {
    private final String url = "http://localhost:8078/";
    private  KVServer kvServer;


    @BeforeEach
    public void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        manager = Managers.getDefault(url);
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }

    @Test
    void saveToFileAndLoadFromFileForEmptyListTest() {
        TaskManager manager2 =  new HTTPTaskManager(url, true);
        assertEquals(manager.getId(), manager.getId(), "Неверно восстановлен Id");
        assertEquals(manager, manager2, "Десериализация менеджера прошла с ошибкой");
    }

    @Test
    void saveToServerAndLoadFromServerWhenEpicsWithoutSubtasksTest() {
        addTasks();
        addEpics();
        manager.getTaskById(1);
        manager.getEpicById(3);
        TaskManager manager2 = new HTTPTaskManager(url, true);
        assertEquals(manager.getId(), manager2.getId(), "Неверно восстановлен Id");
        assertEquals(manager.getHistory(), manager2.getHistory(), "Десериализация менеджера прошла с ошибкой History");
        assertEquals(manager.getTasks(), manager2.getTasks(), "Десериализация менеджера прошла с ошибкой Tasks");
        assertEquals(manager.getEpics(), manager2.getEpics(), "Десериализация менеджера прошла с ошибкой Epics");
        assertEquals(manager.getSubtasks(), manager2.getSubtasks(), "Десериализация менеджера прошла с ошибкой Subtasks");
        assertEquals(manager.getId(), manager2.getId(), "Десериализация менеджера прошла с ошибкой Id");
        assertEquals(manager.getPrioritizedTasks(), manager2.getPrioritizedTasks(), "Десериализация менеджера прошла с ошибкой PrioritizedTasks");
        assertEquals(manager, manager2, "Десериализация менеджера прошла с ошибкой");
    }

    @Test
    void saveToServerAndLoadFromServerWhenHistoryIsEmptyTest() {
        addTasks();
        addEpics();
        TaskManager manager2 = new HTTPTaskManager(url, true);
        assertEquals(manager.getId(), manager.getId(), "Неверно восстановлен Id");
        assertEquals(manager.getHistory(), manager2.getHistory(), "Десериализация менеджера прошла с ошибкой History");
        assertEquals(manager.getTasks(), manager2.getTasks(), "Десериализация менеджера прошла с ошибкой Tasks");
        assertEquals(manager.getEpics(), manager2.getEpics(), "Десериализация менеджера прошла с ошибкой Epics");
        assertEquals(manager.getSubtasks(), manager2.getSubtasks(), "Десериализация менеджера прошла с ошибкой Subtasks");
        assertEquals(manager.getId(), manager2.getId(), "Десериализация менеджера прошла с ошибкой Id");
        assertEquals(manager.getPrioritizedTasks(), manager2.getPrioritizedTasks(), "Десериализация менеджера прошла с ошибкой Id");
        assertEquals(manager, manager2, "Десериализация менеджера прошла с ошибкой");
    }
}
