
package ru.yandex.practicum.management.task;

import ru.yandex.practicum.Status;
import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.exceptions.ManagerSaveException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;
    private static final String TABLE_HEADER = "id,type,name,status,description,epic,startTime,duration(min)";

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        try {
            FileBackedTasksManager fm = new FileBackedTasksManager(file);
            String fileTasks = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            String[] lineTask = fileTasks.split(System.lineSeparator());
            for (int i = 1; i < lineTask.length; i++) {
                if (lineTask[i].isEmpty()) {
                    i++;
                    List<Integer> idHistory = fm.historyFromString(lineTask[i]);
                    for (Integer idTask : idHistory) {
                        if (fm.tasks.containsKey(idTask)) {
                            fm.historyManager.addHistory(fm.tasks.get(idTask));
                        } else if (fm.epics.containsKey(idTask)) {
                            fm.historyManager.addHistory(fm.epics.get(idTask));
                        } else if (fm.subtasks.containsKey(idTask)) {
                            fm.historyManager.addHistory(fm.subtasks.get(idTask));
                        }
                    }
                    return fm;
                } else {
                    fm.fromString(lineTask[i]);
                }
            }
            return fm;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка", e);
        }
    }

    private List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] lineHistory = value.split(",");
        for (String s : lineHistory) {
            history.add(Integer.valueOf(s));
        }
        return history;
    }

    private void fromString(String line) {
        String[] splitTask = line.split(",");

        int idFromCSV = Integer.parseInt(splitTask[0]);
        String name = splitTask[2];
        String status = splitTask[3];
        String description = splitTask[4];
        int duration = Integer.parseInt(splitTask[7]);
        String startTime = splitTask[6];
        findMaxId(idFromCSV);

        switch (splitTask[1]) {
            case "TASK":
                Task task = new Task(name, description, Status.valueOf(status), duration, startTime);
                task.setId(idFromCSV);
                tasks.put(idFromCSV, task);
                break;
            case "EPIC":
                Epic epic = new Epic(name, description);
                epic.setStatus(Status.valueOf(status));
           //     epic.setDuration(Duration.ofMinutes(duration));
                if (startTime.equals("Время ещё не задано")) {
                    epic.setStartTime(null);
                } else {
                    epic.setStartTime(LocalDateTime.parse(startTime, Task.formatter));
                }
                epic.setId(idFromCSV);
                epics.put(idFromCSV, epic);
                break;
            case "SUBTASK":
                Integer idEpic = Integer.valueOf(splitTask[5]);
                Subtask subtask = new Subtask(name, description, Status.valueOf(status), duration, startTime, idEpic);
                subtask.setId(idFromCSV);
                subtasks.put(idFromCSV, subtask);
                epics.get(subtask.getIdEpic()).getListIdSubtask().add(subtask.getId());
                break;
        }
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getPath(), StandardCharsets.UTF_8))) {
            bw.write(TABLE_HEADER);
            for (Task task : tasks.values()) {
                bw.newLine();
                bw.write(task.toString());
            }
            for (Epic epic : epics.values()) {
                bw.newLine();
                bw.write(epic.toString());
            }
            for (Subtask subtask : subtasks.values()) {
                bw.newLine();
                bw.write(subtask.toString());
            }
            bw.newLine();
            bw.newLine();
            bw.write(historyToString(historyManager.getHistory()));

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка", e);
        }
    }

    private static String historyToString(List<Task> history) {
        List<String> idHistoryToString = new ArrayList<>();
        for (Task task : history) {
            idHistoryToString.add(String.valueOf(task.getId()));
        }
        return String.join(",", idHistoryToString);
    }

    private void findMaxId(Integer idFromCSV) {
        if (this.id < idFromCSV) {
            this.id = idFromCSV;
        }
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
    public Task getTaskById(Integer id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
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

