package ru.yandex.practicum.servers.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.domain.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


public class AllTasksHandler extends Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        switch (method) {
            case "GET":
                List<Map<Integer, ? extends Task>> allTasks = List.of(manager.getTasks(), manager.getSubtasks(),
                        manager.getEpics());
                response = gson.toJson(allTasks);
                exchange.sendResponseHeaders(200, 0);
                break;
            case "DELETE":
                manager.deleteAll();
                exchange.sendResponseHeaders(200, 0);
                break;
            default:
                response = "Некорректный метод!";
                exchange.sendResponseHeaders(405, 0);
        }
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}
