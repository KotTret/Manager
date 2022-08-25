package ru.yandex.practicum.management.task;

import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TaskManager {

    Set<Task> getPrioritizedTasks();

    List<Task> getHistory();

    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    Subtask getSubtaskById(Integer id);

    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void addEpic(Epic epic);

    void deleteTaskById(Integer id);

    void deleteSubtaskById(Integer id);

    void deleteEpicById(Integer id);

    List<Subtask> getAllSubtaskOfEpic(Integer id);

    void updateTask(Task task);

    void deleteAll();

    void deleteAllTask();

    void deleteAllEpic();

    void deleteAllSubtask();

    Map<Integer, Task> getTasks();

    Map<Integer, Epic> getEpics();

    Map<Integer, Subtask> getSubtasks();

    int getId();
}
