package ru.yandex.practicum.servers.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.management.task.TaskManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class AllTasksHandler extends Handler {

    public AllTasksHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        switch (method) {
            case "GET":
                List<Map<Integer, ? extends Task>> allTasks = List.of(manager.getTasks(), manager.getSubtasks(),
                        manager.getEpics());
                response = gson.toJson(allTasks);
                exchange.sendResponseHeaders(200, 0);
                writeResponseBody(exchange);
                break;
            case "DELETE":
                manager.deleteAll();
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
                break;
            default:
                response = "Некорректный метод!";
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
        }
    }
}
