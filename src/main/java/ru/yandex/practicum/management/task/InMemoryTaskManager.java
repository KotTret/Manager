package ru.yandex.practicum.management.task;

import ru.yandex.practicum.Status;
import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.exceptions.CollisionTaskException;
import ru.yandex.practicum.management.history.HistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Set<Task> prioritizedTasks = new TreeSet<>();

    protected int id = 0;

    private boolean checkForIntersectionOfTasks(Task newTask) throws CollisionTaskException{
        if (newTask.getStartTime().equals("Время ещё не задано")) {
            return true;
        }
        LocalDateTime startTimeNewTask = LocalDateTime.parse(newTask.getStartTime(), Task.formatter);
        LocalDateTime endTimeNewTask = LocalDateTime.parse(newTask.getEndTime(), Task.formatter);
        for (Task oldTask : prioritizedTasks) {
            LocalDateTime startTimeOldTask = LocalDateTime.parse(oldTask.getStartTime(), Task.formatter);
            LocalDateTime endTimeOldTask = LocalDateTime.parse(oldTask.getEndTime(), Task.formatter);

            if (newTask.getId() == oldTask.getId()) {
                continue;
            } else if (!endTimeNewTask.isAfter(startTimeOldTask) || !startTimeNewTask.isBefore(endTimeOldTask)) {
                continue;
            }
            throw new CollisionTaskException("Невозможно добавить/обновить задачу, это время занято");
        }
        return true;
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
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
        task.setId(id+1);
        if (checkForIntersectionOfTasks(task)) {
            id++;
            tasks.put(id, task);
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(id+1);
        if (!epics.containsKey(subtask.getIdEpic())) {
            System.out.println("Данный Subtask не подходит ни под один Epic");
        } else if (checkForIntersectionOfTasks(subtask)) {
            id++;
            subtasks.put(id, subtask);
            prioritizedTasks.add(subtask);
            epics.get(subtask.getIdEpic()).getListIdSubtask().add(subtask.getId());
            setStatusEpic(epics.get(subtask.getIdEpic()));
            setTimeFrameAndDurationEpic(epics.get(subtask.getIdEpic()));
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
            Task deletedTask = tasks.remove(id);
            historyManager.remove(deletedTask.getId());
            prioritizedTasks.remove(deletedTask);
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
            Subtask deletedSub = subtasks.remove(id);
            historyManager.remove(deletedSub.getId());
            prioritizedTasks.remove(deletedSub);
            setStatusEpic(epics.get(subtask.getIdEpic()));
            setTimeFrameAndDurationEpic(epics.get(subtask.getIdEpic()));
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
                Subtask deletedSub = subtasks.remove(i);
                historyManager.remove(deletedSub.getId());
                prioritizedTasks.remove(deletedSub);
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
            Epic epic = (Epic) task;
            epic.setListIdSubtask(epics.get(epic.getId()).getListIdSubtask());
            epics.put(task.getId(), (Epic) task);
            setStatusEpic(epics.get(epic.getId()));
            setTimeFrameAndDurationEpic(epics.get(epic.getId()));

        } else if (task instanceof Subtask) {
            if (!subtasks.containsKey(task.getId())) {
                return;
            }
            Subtask subtask = (Subtask) task;
            if (checkForIntersectionOfTasks(subtask)) {
                prioritizedTasks.remove(subtasks.get(subtask.getId()));
                subtasks.put(subtask.getId(), subtask);
                prioritizedTasks.add(subtask);
                setStatusEpic(epics.get(subtask.getIdEpic()));
                setTimeFrameAndDurationEpic(epics.get(subtask.getIdEpic()));
            }

        } else {
            if (!tasks.containsKey(task.getId())) {
                return;
            }
            if (checkForIntersectionOfTasks(task)) {
                prioritizedTasks.remove(tasks.get(task.getId()));
                tasks.put(task.getId(), task);
                prioritizedTasks.add(task);
            }
        }
    }

    @Override
    public void deleteAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        historyManager.clear();
        prioritizedTasks.clear();
    }

    @Override
    public void deleteAllTask() {
        for (Integer i : tasks.keySet()) {
            historyManager.remove(i);
            prioritizedTasks.remove(tasks.get(i));
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
            prioritizedTasks.remove(subtasks.get(i));
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

    private void setTimeFrameAndDurationEpic(Epic epic) {
        LocalDateTime startEpic = null;
        LocalDateTime endTimeEpic = null;
        int durationEpic = 0;

        for (int idSubtask : epic.getListIdSubtask()) {
            Subtask subtask = subtasks.get(idSubtask);
            if (!subtask.getStartTime().equals("Время ещё не задано")) {
                LocalDateTime startTime = LocalDateTime.parse(subtask.getStartTime(), Task.formatter);
                LocalDateTime endTime = LocalDateTime.parse(subtask.getEndTime(), Task.formatter);

                if (startEpic == null || startTime.isBefore(startEpic)) {
                    startEpic = startTime;
                }

                if (endTimeEpic == null || endTime.isAfter(endTimeEpic)) {
                    endTimeEpic = endTime;
                }
                durationEpic += subtask.getDuration().toMinutes();
            }
        }
        epic.setStartTime(startEpic);
        epic.setEndTime(endTimeEpic);
        epic.setDuration(Duration.ofMinutes(durationEpic));

/*        if (epic.getListIdSubtask().isEmpty()) {
            epic.setDuration(Duration.ofMinutes(0));
            epic.setEndTime(null);
            epic.setStartTime(null);
        } else {
            Optional<String> startTimeSubtasks = epic.getListIdSubtask().stream().map(subtasks::get).map(Subtask::getStartTime)
                    .filter(s -> !s.equals("Время ещё не задано")).sorted().findFirst();

            Optional<String> endTimeSubtasks = epic.getListIdSubtask().stream().map(subtasks::get).map(Subtask::getStartTime)
                    .filter(s -> !s.equals("Время ещё не задано")).sorted().reduce((first, second) -> second);

            long durationTask = epic.getListIdSubtask().stream().map(subtasks::get).map(Subtask::getDuration)
                    .map(Duration::toMinutes).mapToLong(Long::intValue).sum();
            if (startTimeSubtasks.isEmpty()) {
                epic.setStartTime(null);
                epic.setEndTime(null);
                epic.setDuration(Duration.ofMinutes(0));
            }
            if (endTimeSubtasks.isEmpty()) {
                epic.setEndTime(null);
            }
            if (endTimeSubtasks.isPresent() && startTimeSubtasks.isPresent()) {
                epic.setStartTime(LocalDateTime.parse(startTimeSubtasks.get(), Task.formatter));
                epic.setEndTime(LocalDateTime.parse(endTimeSubtasks.get(), Task.formatter));
                epic.setDuration(Duration.ofMinutes(durationTask));
            }

        }*/
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InMemoryTaskManager)) return false;

        InMemoryTaskManager that = (InMemoryTaskManager) o;

        if (id != that.id) return false;
        if (!historyManager.getHistory().equals(that.historyManager.getHistory())) return false;
        if (!tasks.equals(that.tasks)) return false;
        if (!epics.equals(that.epics)) return false;
        if (!subtasks.equals(that.subtasks)) return false;
        return prioritizedTasks.equals(that.prioritizedTasks);
    }

    @Override
    public int hashCode() {
        int result = historyManager.getHistory().hashCode();
        result = 31 * result + tasks.hashCode();
        result = 31 * result + epics.hashCode();
        result = 31 * result + subtasks.hashCode();
        result = 31 * result + prioritizedTasks.hashCode();
        result = 31 * result + id;
        return result;
    }
}
