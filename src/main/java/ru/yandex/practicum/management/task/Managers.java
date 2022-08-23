package ru.yandex.practicum.management.task;

import ru.yandex.practicum.management.history.HistoryManager;
import ru.yandex.practicum.management.history.InMemoryHistoryManager;

import java.io.File;

public final class Managers {

    public static TaskManager getDefault() {
        return  new InMemoryTaskManager();
    }

    public static TaskManager getFileBackedTasksManager(File file) {
        return  new FileBackedTasksManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
