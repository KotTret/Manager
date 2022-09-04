package ru.yandex.practicum.management.task;

import ru.yandex.practicum.management.history.HistoryManager;
import ru.yandex.practicum.management.history.InMemoryHistoryManager;

import java.io.File;

public final class Managers {

    public static TaskManager getDefault(String url) {
        return  new HTTPTaskManager(url);
    }

    public static TaskManager getFileBackedTasksManager(File file) {
        return  new FileBackedTasksManager(file);
    }

    public static TaskManager getInMemoryTaskManager() {return new InMemoryTaskManager();}

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
