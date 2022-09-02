package ru.yandex.practicum;

import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.management.task.HTTPTaskManager;
import ru.yandex.practicum.servers.KVServer;

import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {

     /*   new KVServer().start();
        HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078/");
        Task task1 = new Task("One", "Test  description", Status.NEW, 30, "10.03.2022 22:12");
        Task task2 = new Task("Two", "Test  description", Status.DONE, 30, "10.03.2022 23:12");
        httpTaskManager.addTask(task1);
        httpTaskManager.addTask(task2);
        httpTaskManager.save();
        httpTaskManager.deleteAll();
        httpTaskManager.loadFromServer();*/

    }
}
