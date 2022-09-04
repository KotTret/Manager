package ru.yandex.practicum.servers.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.exceptions.CollisionTaskException;
import ru.yandex.practicum.management.task.TaskManager;
import java.io.IOException;


public class TasksHandler extends Handler {

    public TasksHandler(TaskManager manager) {
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
            manager.deleteAllTask();
        } else {
            int oldSize = manager.getTasks().size();
            manager.deleteTaskById(Integer.valueOf(idNewTask));
            int newSize = manager.getTasks().size();
            if (!(newSize < oldSize)) {
                rCode = 400;
                response = "Не удалено, id не найден";
            }
        }
    }

    private void createMappingForGET(String idNewTask) {
        if (idNewTask == null) {
            response = gson.toJson(manager.getTasks());
        } else {
            response = gson.toJson(manager.getTaskById(Integer.valueOf(idNewTask)));
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
        Task newTask = gson.fromJson(bodyTask, Task.class);

        if (newTask.getId() == null) {
            try {
                manager.addTask(newTask);
            } catch (CollisionTaskException e) {
                e.printStackTrace();
                rCode = 400;
                response = "Задача не добавлена, время занято";
            }
        } else if (manager.getTasks().containsKey(newTask.getId())) {
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
