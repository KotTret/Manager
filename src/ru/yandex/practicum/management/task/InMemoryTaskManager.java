package ru.yandex.practicum.management.task;

import ru.yandex.practicum.Status;
import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.management.history.HistoryManager;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected int id = 0;

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task getTaskById(Integer id) {
        if (!tasks.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
            return null;
        } else {
            historyManager.addHistory(tasks.get(id));
            return tasks.get(id);
        }
    }

    @Override
    public Epic getEpicById(Integer id) {
        if (!epics.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
            return null;
        } else {
            historyManager.addHistory(epics.get(id));
            return epics.get(id);
        }
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        if (!subtasks.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
            return null;
        } else {
            historyManager.addHistory(subtasks.get(id));
            return subtasks.get(id);
        }
    }

    @Override
    public void addTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getIdEpic())) {
            System.out.println("Данный Subtask не подходит ни под один Epic");
        } else {
            id++;
            subtask.setId(id);
            subtasks.put(id, subtask);
            epics.get(subtask.getIdEpic()).getListIdSubtask().add(subtask.getId());
            setStatusEpic(epics.get(subtask.getIdEpic()));
        }
    }

    @Override
    public void addEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return new HashMap<>(tasks);
    }


    @Override
    public Map<Integer, Epic> getEpics() {
        return new HashMap<>(epics);
    }

    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return new HashMap<>(subtasks);
    }


    @Override
    public void deleteTaskById(Integer id) {
        if (tasks.isEmpty()) {
            System.out.println("Задач в списке нет");
        } else if (!tasks.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
        } else {
            historyManager.remove(tasks.remove(id).getId());
        }
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        if (subtasks.isEmpty()) {
            System.out.println("Задач в списке нет");
        } else if (!subtasks.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
        } else {
            Subtask subtask = subtasks.get(id);
            epics.get(subtask.getIdEpic()).getListIdSubtask().remove(Integer.valueOf(subtask.getId()));
            historyManager.remove(subtasks.remove(id).getId());
            setStatusEpic(epics.get(subtask.getIdEpic()));
        }
    }

    @Override
    public void deleteEpicById(Integer id) {
        if (epics.isEmpty()) {
            System.out.println("Задач в списке нет");
        } else if (!epics.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
        } else {
            for (Integer i : epics.get(id).getListIdSubtask()) {
                historyManager.remove(subtasks.remove(i).getId());
            }
            historyManager.remove(epics.remove(id).getId());
        }
    }

    @Override
    public List<Subtask> getAllSubtaskOfEpic(Integer id) {
        if (!epics.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
            return null;
        } else if (epics.get(id).getListIdSubtask().isEmpty()) {
            System.out.println("Подзадач в списке нет");
            return null;
        } else {
            List<Subtask> subListEpic = new ArrayList<>();
            for (Integer i : epics.get(id).getListIdSubtask()) {
                subListEpic.add(subtasks.get(i));
            }
            return subListEpic;
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task instanceof Epic) {
            if (!epics.containsKey(task.getId())) {
                return;
            }
            epics.put(task.getId(), (Epic) task);
            this.setStatusEpic(epics.get(task.getId()));
        } else if (task instanceof Subtask) {
            if (!subtasks.containsKey(task.getId())) {
                return;
            }
            Subtask subtask = (Subtask) task;
            subtasks.put(subtask.getId(), subtask);
            this.setStatusEpic(epics.get(subtask.getIdEpic()));
        } else {
            if (!tasks.containsKey(task.getId())) {
                return;
            }
            tasks.put(task.getId(), task);
        }

    }

    @Override
    public void deleteAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        historyManager.clear();
    }

    @Override
    public void deleteAllTask() {
        for (Integer i: tasks.keySet()) {
            historyManager.remove(i);
        }
        tasks.clear();

    }

    @Override
    public void deleteAllEpic() {
        for (Integer i: epics.keySet()) {
            historyManager.remove(i);
        }
        epics.clear();
        this.deleteAllSubtask();

    }

    @Override
    public void deleteAllSubtask() {
        for (Integer i: subtasks.keySet()) {
            historyManager.remove(i);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
            epic.getListIdSubtask().clear();
        }
    }

    private void setStatusEpic(Epic epic) {
        epic.setStatus(null);
        for (Integer idSub : epic.getListIdSubtask()) {
            if (subtasks.get(idSub).getStatus() == Status.DONE && (epic.getStatus() == null
                    || epic.getStatus() == Status.DONE)) {
                epic.setStatus(Status.DONE);
            } else if (subtasks.get(idSub).getStatus() == Status.NEW && (epic.getStatus() == null
                    || epic.getStatus() == Status.NEW)) {
                epic.setStatus(Status.NEW);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }
}
