package ru.yandex.practicum.servers.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.management.task.TaskManager;
import java.io.IOException;

public class EpicsHandler extends Handler {

    public EpicsHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        super.handle(exchange);
        switch (method) {
            case "GET":
                createMappingForGET(idNewTask);
                break;
            case "POST":
                createMappingForPOST(bodyTask);
                break;
            case "DELETE":
                createMappingForDELETE(idNewTask);
                break;
            default:
                rCode = 405;
        }
        exchange.sendResponseHeaders(rCode, 0);
        if (response != null)  {
            writeResponseBody(exchange);
        } else {
            exchange.close();
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

    private void createMappingForPOST(String bodyTask) {
        if (bodyTask.isEmpty()) {
            rCode = 400;
            return;
        }
        Epic newTask = gson.fromJson(bodyTask, Epic.class);
        if (newTask.getId() == null) {
                manager.addEpic(newTask);
        } else if (manager.getEpics().containsKey(newTask.getId())) {
                manager.updateTask(newTask);
        } else {
            rCode = 400;
        }
    }
}
