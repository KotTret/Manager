package ru.yandex.practicum;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.practicum.clients.KVTaskClient;
import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.management.task.HTTPTaskManager;
import ru.yandex.practicum.management.task.Managers;
import ru.yandex.practicum.management.task.TaskManager;
import ru.yandex.practicum.servers.HttpTaskServer;
import ru.yandex.practicum.servers.KVServer;

import java.io.IOException;
import java.util.TreeSet;

public class Main {


    public static void main(String[] args) throws IOException {


       // HttpTaskServer httpTaskServer = new HttpTaskServer();
       // httpTaskServer.createAndStartServer();
        new KVServer().start();
        HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078/");
        Task task1 = new Task("One", "Test  description", Status.NEW, 30, "10.03.2022 22:12");
        Task task2 = new Task("Two", "Test  description", Status.DONE, 30, "10.03.2022 23:12");
        httpTaskManager.addTask(task1);
        httpTaskManager.addTask(task2);
        httpTaskManager.save();
        httpTaskManager.deleteAll();
        httpTaskManager.loadFromServer();



    }
}
