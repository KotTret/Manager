package ru.yandex.practicum.management.task;

import ru.yandex.practicum.management.history.HistoryManager;
import ru.yandex.practicum.management.history.InMemoryHistoryManager;

public final class Managers {

    public static TaskManager getDefault() {
        return  new InMemoryTaskManager();
    }

    public static TaskManager getFileBackedTasksManager(String pathCSV) {
        return  new FileBackedTasksManager(pathCSV);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
