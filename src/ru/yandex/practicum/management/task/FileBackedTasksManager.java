package ru.yandex.practicum.management.task;

import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.exceptions.ManagerSaveException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {
/* Не ругайся сильно :). В этот раз тоже немного не понял, зачем статические методы в задании.
Ведь можно же при создании менеджера в консрукторе проверить, есть ли такой файл по  заданному пути,
если есть, то считать его и восстановить данные в менеждер.
    */
    public static void main(String[] args) {
        String separator = File.separator;
        String pathCSV = "resources" + separator + "test.csv";
        TaskManager manager = Managers.getFileBackedTasksManager(pathCSV);
        Task task1 = new Task("1", "buy a book on Java", "NEW");
        manager.addTask(task1);
        Task task2 = new Task("2", "buy a book on Java", "DONE");
        manager.addTask(task2);
        Epic task3 = new Epic("3", "learn english");
        manager.addEpic(task3);
        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getTaskById(2);
        // System.out.println(manager.getHistory());
        //manager.deleteTaskById(1);
    }

    private final Path PATH;
    private static boolean flagReader = true;

    public FileBackedTasksManager(String path) {
        this.PATH = Path.of(path);
        if (Files.exists(PATH)) {
            flagReader = false;
            read();
            flagReader = true;
        }
    }

    private void read() {
        try {
            String fileTasks = Files.readString(Path.of((String.valueOf(PATH))), StandardCharsets.UTF_8);
            String[] lineTask = fileTasks.split(System.lineSeparator());
            for (int i = 1; i < lineTask.length; i++) {
                if (lineTask[i].isEmpty()) {
                    i++;
                    List<Integer> hs = historyFromString(lineTask[i]);
                    for (Integer y : hs) {
                        if (tasks.containsKey(y)) {
                            historyManager.addHistory(tasks.get(y));
                        } else if (epics.containsKey(y)) {
                            historyManager.addHistory(epics.get(y));
                        } else if (subtasks.containsKey(y)) {
                            historyManager.addHistory(subtasks.get(y));
                        }
                    }
                    return;
                } else {
                    fromString(lineTask[i]);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка");
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

        int id = Integer.parseInt(splitTask[0]);
        String name = splitTask[2];
        String status = splitTask[3];
        String description = splitTask[4];

        switch (splitTask[1]) {
            case "Task":
                Task task = new Task(name, description, status);
                task.setId(id);
                if (this.id < id) {
                    this.id = id;
                }
                addTask(task);
                break;
            case "Epic":
                Epic epic = new Epic(name, description);
                epic.setId(id);
                if (this.id < id) {
                    this.id = id;
                }
                addEpic(epic);
                break;
            case "Subtask":
                Integer idEpic = Integer.valueOf(splitTask[5]);
                Subtask subtask = new Subtask(name, description, status, idEpic);
                subtask.setId(id);
                if (this.id < id) {
                    this.id = id;
                }
                addSubtask(subtask);
                break;
        }
    }

    private void save() {
        if (!flagReader) {
            return;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(String.valueOf(PATH), StandardCharsets.UTF_8))) {
            String TABLE_HEADER = "id,type,name,status,description,epic";
            bw.write(TABLE_HEADER);
            for (Task task : getTasks().values()) {
                bw.newLine();
                bw.write(task.toString());
            }
            for (Epic epic : getEpics().values()) {
                bw.newLine();
                bw.write(epic.toString());
            }
            for (Subtask subtask : getSubtasks().values()) {
                bw.newLine();
                bw.write(subtask.toString());
            }
            bw.newLine();
            bw.newLine();
            for (Task task : getHistory()) {
                bw.write(task.getId() + ",");
            }

        } catch (IOException ex) {
            throw new ManagerSaveException("Ошибка");
        }
    }

    @Override
    public void addTask(Task task) {
        if (!flagReader) {
            tasks.put(task.getId(), task);
        } else {
            super.addTask(task);
            save();
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (!flagReader) {
            epics.put(epic.getId(), epic);
        } else {
            super.addEpic(epic);
            save();
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (!flagReader) {
            subtasks.put(subtask.getId(), subtask);
        } else {
            super.addSubtask(subtask);
            save();
        }
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
