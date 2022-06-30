package ru.yandex.practicum.management.history;

import ru.yandex.practicum.domain.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_LENGTH_HISTOrY = 10;
    private final List<Task> browsingHistoryTask = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(browsingHistoryTask);
    }

    @Override
    public void addHistory(Task task) {
        if (browsingHistoryTask.size() == MAX_LENGTH_HISTOrY) {
            browsingHistoryTask.remove(0);
        }
        browsingHistoryTask.add(task);
    }
}
