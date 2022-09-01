package ru.yandex.practicum.management.task;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.clients.KVTaskClient;
import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.management.history.HistoryManager;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class HTTPTaskManager extends InMemoryTaskManager{

    private final KVTaskClient kvTaskClient;
    private static Gson gson;
    private final String tasksKey = "tasks";
    private final String epicsKey = "epics";
    private final String subtasksKey = "subtasks";
    private final String historyKey = "history";


/*    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final Set<Task> prioritizedTasks = new TreeSet<>();

    protected int id = 0;*/

    public HTTPTaskManager(String url) {
        kvTaskClient = new KVTaskClient(url);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        loadFromServer();
    }

    public void save() {
        String tasksJson = gson.toJson(tasks);
        String epicsJson = gson.toJson(epics);
        String subtaskJson = gson.toJson(subtasks);
        kvTaskClient.put(tasksKey, tasksJson);
        kvTaskClient.put(epicsKey, epicsJson);
        kvTaskClient.put(subtasksKey, subtaskJson);

        String historyJson = gson.toJson(historyManager.getHistory().stream()
                .map(Task::getId)
                .collect(Collectors.toList()));
        kvTaskClient.put(historyKey, historyJson);
    }

    public void loadFromServer() {

        String tasksJson = kvTaskClient.load(tasksKey);
        HashMap<Integer,Task> tasksFromServer = gson.fromJson(tasksJson, new TypeToken<HashMap<Integer, Task>>(){}.getType());
        if (tasksFromServer != null) {
            for (Task task: tasksFromServer.values()) {
                this.tasks.put(task.getId(), task);
                findMaxId(task.getId());
                prioritizedTasks.add(task);
            }
        }

        String epicJson = kvTaskClient.load(epicsKey);
        HashMap<Integer,Epic> epicsFromServer = gson.fromJson(epicJson, new TypeToken<HashMap<Integer, Epic>>(){}.getType());
        if (epicsFromServer != null) {
            for (Epic epic: epicsFromServer.values()) {
                this.epics.put(epic.getId(), epic);
                findMaxId(epic.getId());
            }
        }

        String subtasksJson = kvTaskClient.load(subtasksKey);
        HashMap<Integer,Subtask> subtasksFromServer = gson.fromJson(subtasksJson, new TypeToken<HashMap<Integer, Subtask>>(){}.getType());
        if (subtasksFromServer !=null) {
            for (Subtask subtask: subtasksFromServer.values()) {
                this.subtasks.put(subtask.getId(), subtask);
                findMaxId(subtask.getId());
                prioritizedTasks.add(subtask);
            }
        }

        String historyJson = kvTaskClient.load(historyKey);
        List<Integer> historyIdFromServer = gson.fromJson(historyJson, new TypeToken<ArrayList<Integer>>(){}.getType());
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

    private void findMaxId(Integer idFromServer) {
        if (this.id < idFromServer) {
            this.id = idFromServer;
        }
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task =  super.getTaskById(id);
        save();
        return task;

    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic =  super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask =  super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }
}
