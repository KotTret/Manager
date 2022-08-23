package ru.yandex.practicum.management.task;

import ru.yandex.practicum.Status;
import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.management.history.HistoryManager;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();


    protected int id = 0;


    private boolean checkForIntersectionOfTasks(Task newTask) {
        TreeSet<Task> check = getPrioritizedTasks();
        for (Task task : check) {
            if (newTask.equals(task) || (newTask instanceof Epic && task instanceof Subtask)) {
                continue;
            }
            if (newTask.getStartTime().equals("Время ещё не задано")) {
                return true;
            } else if (task.getStartTime().equals("Время ещё не задано")) {
                continue;
            } else if (LocalDateTime.parse(newTask.getStartTime(), Task.formatter).isBefore(LocalDateTime.parse(task.getEndTime(), Task.formatter)) &&
                    LocalDateTime.parse(newTask.getStartTime(), Task.formatter).isAfter(LocalDateTime.parse(task.getStartTime(), Task.formatter))) {
                return false;
            } else if (LocalDateTime.parse(newTask.getEndTime(), Task.formatter).isAfter(LocalDateTime.parse(task.getStartTime(), Task.formatter)) &&
                    LocalDateTime.parse(newTask.getStartTime(), Task.formatter).isBefore(LocalDateTime.parse(task.getStartTime(), Task.formatter))) {
                return false;
            }  else if (LocalDateTime.parse(newTask.getStartTime(), Task.formatter).equals(LocalDateTime.parse(task.getStartTime(), Task.formatter)) ||
                    LocalDateTime.parse(newTask.getEndTime(), Task.formatter).equals(LocalDateTime.parse(task.getEndTime(), Task.formatter))) {
                return false;
            }
        }
        return true;
    }
    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        TreeSet<Task> prioritizedTasks = new TreeSet<>();
        prioritizedTasks.addAll(tasks.values());
        prioritizedTasks.addAll(epics.values());
        prioritizedTasks.addAll(subtasks.values());
        return new TreeSet<>(prioritizedTasks);
    }


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
        if (checkForIntersectionOfTasks(task)) {
            tasks.put(id, task);
        } else {
            id--;
            System.out.println("Нельза добавить задачу, на это время Вы заняты!!!");
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        if (!epics.containsKey(subtask.getIdEpic())) {
            id--;
            System.out.println("Данный Subtask не подходит ни под один Epic");
        } else if(!checkForIntersectionOfTasks(subtask)) {
            id--;
            System.out.println("Нельза добавить задачу, на это время Вы заняты!!!");
        } else {
            subtasks.put(id, subtask);
            epics.get(subtask.getIdEpic()).getListIdSubtask().add(subtask.getId());
            setStatusEpic(epics.get(subtask.getIdEpic()));
            setStartAndEndTimeEpic(epics.get(subtask.getIdEpic()));
        }
    }

    @Override
    public void addEpic(Epic epic) {
        id++;
        epic.setId(id);
        if (checkForIntersectionOfTasks(epic)) {
            epics.put(id, epic);
        } else {
            id--;
            System.out.println("Нельза добавить задачу, на это время Вы заняты!!!");
        }
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
            setStartAndEndTimeEpic(epics.get(subtask.getIdEpic()));
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
            if (checkForIntersectionOfTasks(task)) {
                epics.put(task.getId(), (Epic) task);
                this.setStatusEpic(epics.get(task.getId()));
                this.setStartAndEndTimeEpic(epics.get(task.getId()));
            } else {
                System.out.println("Невозможно обновить задачу, на это время Вы заняты!!!");
            }

        } else if (task instanceof Subtask) {
            if (!subtasks.containsKey(task.getId())) {
                return;
            }
            Subtask subtask = (Subtask) task;
            Subtask deletedSubtask = subtasks.get(subtask.getId());
            subtasks.put(subtask.getId(), subtask);
            this.setStatusEpic(epics.get(subtask.getIdEpic()));
            this.setStartAndEndTimeEpic(epics.get(subtask.getIdEpic()));
            if (!checkForIntersectionOfTasks(epics.get(subtask.getIdEpic()))) {
                subtasks.put(deletedSubtask.getId(), deletedSubtask);
                this.setStatusEpic(epics.get(deletedSubtask.getIdEpic()));
                this.setStartAndEndTimeEpic(epics.get(deletedSubtask.getIdEpic()));
                System.out.println("Невозможно обновить задачу, на это время Вы заняты!!!");
            }
        } else {
            if (!tasks.containsKey(task.getId())) {
                return;
            }
            if (checkForIntersectionOfTasks(task)) {
                tasks.put(task.getId(), task);
            } else {
                System.out.println("Невозможно обновить задачу, на это время Вы заняты!!!");
            }
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
        for (Integer i : tasks.keySet()) {
            historyManager.remove(i);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpic() {
        for (Integer i : epics.keySet()) {
            historyManager.remove(i);
        }
        epics.clear();
        this.deleteAllSubtask();

    }

    @Override
    public void deleteAllSubtask() {
        for (Integer i : subtasks.keySet()) {
            historyManager.remove(i);
        }
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
            epic.setStartTime(null);
            epic.setEndTime(null);
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


    private void setStartAndEndTimeEpic(Epic epic) {
        List<String> startTimeSubtasks = epic.getListIdSubtask().stream().map(subtasks::get).map(Subtask::getStartTime)
                .sorted().collect(Collectors.toCollection(ArrayList::new));
        List<String> endTimeSubtasks = epic.getListIdSubtask().stream().map(subtasks::get).map(Subtask::getEndTime)
                .sorted().collect(Collectors.toCollection(ArrayList::new));
        epic.setStartTime(LocalDateTime.parse(startTimeSubtasks.get(0), Task.formatter));
        epic.setEndTime((LocalDateTime.parse(endTimeSubtasks.get(endTimeSubtasks.size() - 1), Task.formatter)));
        epic.setDuration();

    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InMemoryTaskManager that = (InMemoryTaskManager) o;

        if (id != that.id) return false;
        if (!historyManager.equals(that.historyManager)) return false;
        if (!tasks.equals(that.tasks)) return false;
        if (!epics.equals(that.epics)) return false;
        return subtasks.equals(that.subtasks);
    }

    @Override
    public int hashCode() {
        int result = historyManager.hashCode();
        result = result + tasks.hashCode();
        result = result + epics.hashCode();
        result = result + subtasks.hashCode();
        result = result + id;
        return result;
    }
}
