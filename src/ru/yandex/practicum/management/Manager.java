package ru.yandex.practicum.management;

import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;

import java.util.*;

public class Manager {
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    private int id = 0;

    public Task getTaskById(Integer id) {
        if (!taskList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
            return null;
        } else {
            return taskList.get(id);
        }
    }

    public Epic getEpicById(Integer id) {
        if (!epicList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
            return null;
        } else {
            return epicList.get(id);
        }
    }

    public Subtask getSubtaskById(Integer id) {
        if (!subtaskList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
            return null;
        } else {
            return subtaskList.get(id);
        }
    }

    public void addTask(Task task) {
        id++;
        task.setId(id);
        taskList.put(id, task);
    }

    public void addSubtask(Subtask subtask) {
        if (!epicList.containsKey(subtask.getIdEpic())) {
            System.out.println("Данный Subtask не подходит ни под один Epic");
        } else {
            id++;
            subtask.setId(id);
            subtaskList.put(id, subtask);
            epicList.get(subtask.getIdEpic()).getListIdSubtask().add(subtask.getId());
            setStatusEpic(epicList.get(subtask.getIdEpic()));
        }
    }

    public void addEpic(Epic epic) {
        id++;
        epic.setId(id);
        epicList.put(id, epic);
    }

    public HashMap<Integer, Task> getTaskList() {
        return new HashMap<>(taskList);
    }

    public HashMap<Integer, Epic> getEpicList() {
        return new HashMap<>(epicList);
    }

    public HashMap<Integer, Subtask> getSubtaskList() {
        return new HashMap<>(subtaskList);
    }


    public void deleteTaskById(Integer id) {
        if (taskList.isEmpty()) {
            System.out.println("Задач в списке нет");
        } else if (!taskList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
        } else {
            taskList.remove(id);
        }
    }

    public void deleteSubtaskById(Integer id) {
        if (subtaskList.isEmpty()) {
            System.out.println("Задач в списке нет");
        } else if (!subtaskList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
        } else {
            Subtask subtask = subtaskList.get(id);
            epicList.get(subtask.getIdEpic()).getListIdSubtask().remove(Integer.valueOf(subtask.getId()));
            subtaskList.remove(id);
            setStatusEpic(epicList.get(subtask.getIdEpic()));
        }
    }

    public void deleteEpicById(Integer id) {
        if (epicList.isEmpty()) {
            System.out.println("Задач в списке нет");
        } else if (!epicList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
        } else {
            HashMap<Integer, Subtask> subtaskListHelp = new HashMap<>(subtaskList);
            for (Subtask s : subtaskListHelp.values()) {
                if (s.getIdEpic().equals(id)) {
                    subtaskList.remove(s.getId());
                }
            }
            epicList.remove(id);
        }
    }

    public List<Subtask> getAllSubtaskOfEpic(Integer id) {
        if (!epicList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
            return null;
        } else if (epicList.get(id).getListIdSubtask().isEmpty()) {
            System.out.println("Подзадач в списке нет");
            return null;
        } else {
            List<Subtask> subListEpic = new ArrayList<>();
            for (Subtask sub : subtaskList.values()) {
                if (sub.getIdEpic().equals(id)) {
                    subListEpic.add(sub);
                }
            }
            return subListEpic;
        }
    }

    public void updateTask(Task task) {
        if (task instanceof Epic) {
            if (!epicList.containsKey(task.getId())) {
                return;
            }
            epicList.put(task.getId(), (Epic) task);
            this.setStatusEpic(epicList.get(task.getId()));
        } else if (task instanceof Subtask) {
            if (!subtaskList.containsKey(task.getId())) {
                return;
            }
            Subtask subtask = (Subtask) task;
            subtaskList.put(subtask.getId(), subtask);
            this.setStatusEpic(epicList.get(subtask.getIdEpic()));
        } else {
            if (!taskList.containsKey(task.getId())) {
                return;
            }
            taskList.put(task.getId(), task);
        }

    }

    public void deleteAll() {
        taskList.clear();
        epicList.clear();
        subtaskList.clear();
    }

    public void deleteAllTask() {
        taskList.clear();
    }

    public void deleteAllEpic() {
        epicList.clear();
        this.deleteAllSubtask();
    }

    public void deleteAllSubtask() {
        subtaskList.clear();
        for (Epic epic : epicList.values()) {
            epic.setStatus("NEW");
        }
    }

    public void setStatusEpic(Epic epic) {
        epic.setStatus(null);
        for (Integer idSub : epic.getListIdSubtask()) {
            if (subtaskList.get(idSub).getStatus().equals("DONE") && (epic.getStatus() == null
                    || epic.getStatus().equals("DONE"))) {
                epic.setStatus("DONE");
            } else if (subtaskList.get(idSub).getStatus().equals("NEW") && (epic.getStatus() == null
                    || epic.getStatus().equals("NEW"))) {
                epic.setStatus("NEW");
            } else {
                epic.setStatus("IN_PROGRESS");
            }
        }
    }
}
