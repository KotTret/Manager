package ru.yandex.practicum.management.task;

import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.management.history.HistoryManager;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    public HistoryManager getHistoryManager();

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

    HashMap<Integer, Task> getTaskList();

    HashMap<Integer, Epic> getEpicList();

    HashMap<Integer, Subtask> getSubtaskList();

}
