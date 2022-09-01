package ru.yandex.practicum.servers;
import com.sun.net.httpserver.HttpServer;

import ru.yandex.practicum.management.task.Managers;
import ru.yandex.practicum.management.task.TaskManager;
import ru.yandex.practicum.servers.handlers.*;

import java.io.IOException;

import java.net.InetSocketAddress;


public class HttpTaskServer {
    HttpServer httpServer;
    private static final int PORT = 8080;
    private static final String url = "http://localhost:8078/";
    public static final TaskManager manager = Managers.getDefault(url);

    public void createAndStartServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/", new AllTasksHandler());
        httpServer.createContext("/tasks/task", new TasksHandler());
        httpServer.createContext("/tasks/subtask", new SubtasksHandler());
        httpServer.createContext("/tasks/epic", new EpicsHandler());
        httpServer.createContext("/tasks/history", new HistoryHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(1);
    }

}


