package ru.yandex.practicum.management;

import ru.yandex.practicum.Status;
import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager{
    private final HashMap<Integer, Task> taskList = new HashMap<>();
    private final HashMap<Integer, Epic> epicList = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    private final ArrayList<Task> browsingHistoryTask = new ArrayList<>();

    private int id = 0;
    private int iter = 0;


    @Override
    public List<Task> getHistory() {
        return browsingHistoryTask;
    }

    private void addHistory(Task task) {
        if (browsingHistoryTask.size() == 10) {
            browsingHistoryTask.add(iter++, task);
            if (iter == 9) {iter = 0;}
        } else {
            browsingHistoryTask.add(task);
        }
    }

    @Override
    public Task getTaskById(Integer id) {
        if (!taskList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
            return null;
        } else {
            addHistory(taskList.get(id));
            return taskList.get(id);
        }
    }

    @Override
    public Epic getEpicById(Integer id) {
        if (!epicList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
            return null;
        } else {
            addHistory(epicList.get(id));
            return epicList.get(id);

        }
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        if (!subtaskList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
            return null;
        } else {
            addHistory(subtaskList.get(id));
            return subtaskList.get(id);
        }
    }

    @Override
    public void addTask(Task task) {
        id++;
        task.setId(id);
        taskList.put(id, task);
    }

    @Override
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

    @Override
    public void addEpic(Epic epic) {
        id++;
        epic.setId(id);
        epicList.put(id, epic);
    }

    public HashMap<Integer, Task> getTaskList() {
        for (Task task: taskList.values()) {
            addHistory(task);
        }
        return new HashMap<>(taskList);
    }

    public HashMap<Integer, Epic> getEpicList() {
        for (Epic epic : epicList.values()) {
            addHistory(epic);
        }
        return new HashMap<>(epicList);
    }

    public HashMap<Integer, Subtask> getSubtaskList() {
        for (Subtask subtask: subtaskList.values()) {
            addHistory(subtask);
        }
        return new HashMap<>(subtaskList);
    }


    @Override
    public void deleteTaskById(Integer id) {
        if (taskList.isEmpty()) {
            System.out.println("Задач в списке нет");
        } else if (!taskList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
        } else {
            taskList.remove(id);
        }
    }

    @Override
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

    @Override
    public void deleteEpicById(Integer id) {
        if (epicList.isEmpty()) {
            System.out.println("Задач в списке нет");
        } else if (!epicList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
        } else {
            for (Integer i : epicList.get(id).getListIdSubtask()) {
                subtaskList.remove(i);
            }
            epicList.remove(id);
        }
    }

    @Override
    public List<Subtask> getAllSubtaskOfEpic(Integer id) {
        if (!epicList.containsKey(id)) {
            System.out.println("Задач с таким идентификатором не найдено");
            return null;
        } else if (epicList.get(id).getListIdSubtask().isEmpty()) {
            System.out.println("Подзадач в списке нет");
            return null;
        } else {
            List<Subtask> subListEpic = new ArrayList<>();
            for (Integer i : epicList.get(id).getListIdSubtask()) {
                subListEpic.add(subtaskList.get(i));
                addHistory(subtaskList.get(i));
            }
            return subListEpic;
        }
    }

    @Override
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

    @Override
    public void deleteAll() {
        taskList.clear();
        epicList.clear();
        subtaskList.clear();
    }

    @Override
    public void deleteAllTask() {
        taskList.clear();
    }

    @Override
    public void deleteAllEpic() {
        epicList.clear();
        this.deleteAllSubtask();
    }

    @Override
    public void deleteAllSubtask() {
        subtaskList.clear();
        for (Epic epic : epicList.values()) {
            epic.setStatus(Status.NEW);
            epic.getListIdSubtask().clear();
        }
    }

    private void setStatusEpic(Epic epic) {
        epic.setStatus(null);
        for (Integer idSub : epic.getListIdSubtask()) {
            if (subtaskList.get(idSub).getStatus() == Status.DONE && (epic.getStatus() == null
                    || epic.getStatus() == Status.DONE)) {
                epic.setStatus(Status.DONE);
            } else if (subtaskList.get(idSub).getStatus() == Status.NEW && (epic.getStatus() == null
                    || epic.getStatus() == Status.NEW)) {
                epic.setStatus(Status.NEW);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }
}
