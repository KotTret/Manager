package ru.yandex.practicum.management;

import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;

import java.util.HashMap;
import java.util.List;

public class Manager {
    private HashMap<Integer, Task> taskList;
    private int id = 0;

    public Task getTaskById(Integer id) {
        if (taskList == null) {
            System.out.println("Задач в списке нет");
            return null;
        } else if (!taskList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
            return null;
        } else {
            return taskList.get(id);
        }
    }

    public void addTask(Task task) {
        id++;
        task.setId(id);
        if (taskList == null) {
            taskList = new HashMap<>();
            taskList.put(id, task);
        } else {
            taskList.put(id, task);
        }
    }

    public HashMap<Integer, Task> getTaskList() {
        if (taskList != null) {
            return new HashMap<>(taskList);
        } else {
            return null;
        }
    }

    public void deleteAllTasks() {
        taskList = null;
    }

    public void deleteTasksById(Integer id) {
        if (taskList == null) {
            System.out.println("Задач в списке нет");
        } else if (!taskList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
        } else {
            taskList.remove(id);
        }
    }

    public List<Subtask> getAllSubtask(Integer id) {
        if (!taskList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
            return null;
        } else if (taskList.get(id) instanceof Epic) {
            Epic epic = (Epic) taskList.get(id);
            return epic.getListSubtask();
        } else {
            System.out.println("У этой задачи нет подзадач");
            return null;
        }
    }

    public void updateTask(Task task, Integer id) {
        /*здесь, если честно, не совсем понятно, десятки раз перечитал задание, я примерно догадываюсь, что здесь
        * заложена замена через сравнение объектов, может это и надо будет дальше в реализации, но раз
        * мы используем Id как неповторимый, и по нему мы различаем задачи, то почему бы не обновлять задачу,
        * указывая конкретный Id задачи, который мы хотим обновить*/
        if (taskList == null) {
            System.out.println("Задач в списке нет");
        } else if (!taskList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
        } else {
            task.setId(id);
            taskList.put(id, task);
        }
    }
}
