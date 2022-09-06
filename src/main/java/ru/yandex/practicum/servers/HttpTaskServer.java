package ru.yandex.practicum.servers;
import com.sun.net.httpserver.HttpServer;

import ru.yandex.practicum.management.task.Managers;
import ru.yandex.practicum.management.task.TaskManager;
import ru.yandex.practicum.servers.handlers.*;

import java.io.IOException;

import java.net.InetSocketAddress;


public class HttpTaskServer {
    private HttpServer httpServer;
    private static final int PORT = 8080;
    private static final String URL = "http://localhost:8078/";
    private final TaskManager manager = Managers.getDefault(URL);

    public void createServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/", new AllTasksHandler(manager));
        httpServer.createContext("/tasks/task", new TasksHandler(manager));
        httpServer.createContext("/tasks/subtask", new SubtasksHandler(manager));
        httpServer.createContext("/tasks/epic", new EpicsHandler(manager));
        httpServer.createContext("/tasks/history", new HistoryHandler(manager));
    }

    public  void  start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public TaskManager getManager() {
        return manager;
    }

    public void stop() {
        httpServer.stop(1);
    }
}


