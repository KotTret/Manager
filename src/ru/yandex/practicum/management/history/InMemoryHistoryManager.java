package ru.yandex.practicum.management.history;

import ru.yandex.practicum.domain.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> browsingHistoryTask = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return browsingHistoryTask;
    }

    @Override
    public void addHistory(Task task) {
        if (browsingHistoryTask.size() == 10) {
            browsingHistoryTask.remove(0);
            browsingHistoryTask.add(task);
        } else {
            browsingHistoryTask.add(task);
        }
    }
}
