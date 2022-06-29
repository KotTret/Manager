package ru.yandex.practicum.management.history;

import ru.yandex.practicum.domain.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void addHistory(Task task);
}
