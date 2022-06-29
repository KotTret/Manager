package ru.yandex.practicum.management.task;

import ru.yandex.practicum.management.history.HistoryManager;

public abstract class Managers {

    public static TaskManager getDefault(TaskManager manager) {
        return manager;
    }

    public static HistoryManager getDefaultHistory(HistoryManager historyManager) {
        return historyManager;
    }
}

