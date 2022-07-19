package ru.yandex.practicum.management.history;

import ru.yandex.practicum.domain.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {


    private final Set<Task> browsingHistoryTask = new LinkedHashSet();
/*    Привет, не обижайся, что тут пишу :). Я что-то не понял танцы с бубнами в описании выпонения задания.
    Цель, которая стоит в задании "реализовать функциональность так, чтобы время просмотра задачи никак не зависело от общего количества задач в истории"
    Но есть же LinkedHashSet, у которого сложность по основным операциям, такая же как у HashSet,
    и составляет  O(1) всегда, также защищает от повторений и выдерживает последовательность,  и при удалении
    очень удобно метод remove у HashMap возвращает объект, и мы его сразу передаем в remove LinkedHashSet.
    Ну или я не до конца понял теорию (О_о)
    */

    @Override
    public Set<Task> getHistory() {
        return new LinkedHashSet<>(browsingHistoryTask);
    }

    @Override
    public void addHistory(Task task) {
        if (browsingHistoryTask.contains(task)) {
            browsingHistoryTask.remove(task);
            browsingHistoryTask.add(task);
        } else {
            browsingHistoryTask.add(task);
        }

    }

    @Override
    public void remove(Task task) {
        browsingHistoryTask.remove(task);
    }

    @Override
    public void clear() {
        browsingHistoryTask.clear();
    }

}
