package ru.yandex.practicum.servers.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.management.task.TaskManager;
import java.io.IOException;

public class HistoryHandler extends Handler {

    public HistoryHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        switch (method) {
            case "GET":
                response = gson.toJson(manager.getHistory());
                exchange.sendResponseHeaders(200, 0);
                writeResponseBody(exchange);
                break;
            default:
                response = "Некорректный метод!";
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
        }
    }
}
