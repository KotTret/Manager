package ru.yandex.practicum.servers.handlers;

import com.sun.net.httpserver.HttpExchange;

import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.domain.Epic;

import ru.yandex.practicum.exceptions.CollisionTaskException;



import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class EpicsHandler extends Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        switch (method) {
            case "GET":
                createMappingForGET(idNewTask);
                break;
            case "POST":
                createMappingForPOST(idNewTask, bodyTask);
                break;
            case "DELETE":
                createMappingForDELETE(idNewTask);
                break;
            default:
                rCode = 405;
        }
        exchange.sendResponseHeaders(rCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }

    private void createMappingForDELETE(String idNewTask) {
        if (idNewTask == null) {
            manager.deleteAllEpic();
        } else {
            int oldSize = manager.getEpics().size();
            manager.deleteEpicById(Integer.valueOf(idNewTask));
            int newSize = manager.getEpics().size();
            if (!(newSize < oldSize)) {
                rCode = 400;
                response = "Не удалено, id не найден";
            }
        }
    }

    private void createMappingForGET(String idNewTask) {
        if (idNewTask == null) {
            response = gson.toJson(manager.getEpics());
        } else {
            response = gson.toJson(manager.getEpicById(Integer.valueOf(idNewTask)));
            if (response.equals("null")) {
                rCode = 400;
                response = "Задача не найдена";
            } else {
                rCode = 200;
            }
        }
    }

    private void createMappingForPOST(String idNewTask,  String bodyTask) {
        Epic newTask = gson.fromJson(bodyTask, Epic.class);
        if (idNewTask == null) {
            try {
                manager.addEpic(newTask);
            } catch (CollisionTaskException e) {
                e.printStackTrace();
                rCode = 400;
                response = "Задача не добавлена, время занято";
            }
        } else if (newTask.getId() == null ||!idNewTask.equals(String.valueOf(newTask.getId()))) {
            rCode = 400;
            response = "Переданный id и id в теле запроса не совпадают";
        } else if (manager.getEpics().containsKey(newTask.getId())) {
            try {
                manager.updateTask(newTask);
            } catch (CollisionTaskException e) {
                e.printStackTrace();
                rCode = 400;
                response = "Задача не обвновлена, время занято";
            }
        } else {
            rCode = 400;
        }
    }
}
