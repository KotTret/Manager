package ru.yandex.practicum.management.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.clients.KVTaskClient;
import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;

import java.util.*;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {

    private final KVTaskClient kvTaskClient;
    private static Gson gson;
    private static final String TASKS_KEY = "tasks";
    private static final String EPICS_KEY = "epics";
    private static final String SUBTASKS_KEY = "subtasks";
    private static final String HISTORY_KEY = "history";




    public HTTPTaskManager(String uri, boolean isLoadFromServer) {
        super(null);
        kvTaskClient = new KVTaskClient(uri);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        if (isLoadFromServer) {
            loadFromServer();
        }
        save();
    }

    public HTTPTaskManager(String uri) {
        this(uri, false);
    }

    @Override
    public void save() {
        String tasksJson = gson.toJson(tasks);
        String epicsJson = gson.toJson(epics);
        String subtaskJson = gson.toJson(subtasks);
        kvTaskClient.put(TASKS_KEY, tasksJson);
        kvTaskClient.put(EPICS_KEY, epicsJson);
        kvTaskClient.put(SUBTASKS_KEY, subtaskJson);

        String historyJson = gson.toJson(historyManager.getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList()));
        kvTaskClient.put(HISTORY_KEY, historyJson);
    }

    public void loadFromServer() {

        String tasksJson = kvTaskClient.load(TASKS_KEY);
        HashMap<Integer, Task> tasksFromServer = gson.fromJson(tasksJson, new TypeToken<HashMap<Integer, Task>>() {
        }.getType());
        if (tasksFromServer != null) {
            for (Task task : tasksFromServer.values()) {
                this.tasks.put(task.getId(), task);
                findMaxId(task.getId());
                prioritizedTasks.add(task);
            }
        }

        String epicJson = kvTaskClient.load(EPICS_KEY);
        HashMap<Integer, Epic> epicsFromServer = gson.fromJson(epicJson, new TypeToken<HashMap<Integer, Epic>>() {
        }.getType());
        if (epicsFromServer != null) {
            for (Epic epic : epicsFromServer.values()) {
                this.epics.put(epic.getId(), epic);
                findMaxId(epic.getId());
            }
        }

        String subtasksJson = kvTaskClient.load(SUBTASKS_KEY);
        HashMap<Integer, Subtask> subtasksFromServer = gson.fromJson(subtasksJson, new TypeToken<HashMap<Integer, Subtask>>() {
        }.getType());
        if (subtasksFromServer != null) {
            for (Subtask subtask : subtasksFromServer.values()) {
                this.subtasks.put(subtask.getId(), subtask);
                findMaxId(subtask.getId());
                prioritizedTasks.add(subtask);
            }
        }

        String historyJson = kvTaskClient.load(HISTORY_KEY);
        List<Integer> historyIdFromServer = gson.fromJson(historyJson, new TypeToken<ArrayList<Integer>>() {
        }.getType());
        if (historyIdFromServer != null) {
            for (Integer idTask : historyIdFromServer) {
                if (tasks.containsKey(idTask)) {
                    historyManager.addHistory(tasks.get(idTask));
                } else if (epics.containsKey(idTask)) {
                    historyManager.addHistory(epics.get(idTask));
                } else if (subtasks.containsKey(idTask)) {
                    historyManager.addHistory(subtasks.get(idTask));
                }
            }
        }
    }
}
