package ru.yandex.practicum.management.history;

import ru.yandex.practicum.domain.Task;

import java.util.Set;

public interface HistoryManager {

    Set<Task> getHistory();

    void addHistory(Task task);

    void remove(Task task);

    void clear();

}
