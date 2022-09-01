package ru.yandex.practicum.servers.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.management.task.HTTPTaskManager;
import ru.yandex.practicum.servers.HttpTaskServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


abstract class Handler implements HttpHandler {
    protected HTTPTaskManager manager = (HTTPTaskManager) HttpTaskServer.manager;
    protected Gson gson;
    protected String response;
    protected String method;
    protected Headers headers;
    int rCode;
    protected String bodyTask;
    protected String idNewTask;


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        manager.loadFromServer();
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        method = exchange.getRequestMethod();
        headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json");
        response = "";
        rCode = 200;
        bodyTask = checkAndReturnPassedRequestParameters(exchange).get(0);
        idNewTask = checkAndReturnPassedRequestParameters(exchange).get(1);
    }

    protected ArrayList<String> checkAndReturnPassedRequestParameters(HttpExchange exchange) throws IOException {
        String URI = exchange.getRequestURI().toString();
        Map<String, String> param = new HashMap<>();
        String[] components = URI.split("[\\?\\&]");

        for (String component : components) {
            if (component.length() == 0) continue;
            int eq = component.indexOf('=');
            if (eq > 0) {
                String name = component.substring(0, eq);
                String value = component.substring(eq + 1, component.length());
                param.put(name, value);
            } else {
                param.put(component, "");
            }
        }
        String idParam = param.get("id");
        String idNewTask = null;
        if (idParam != null) {
            idNewTask = idParam;
        }
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes());
        ArrayList<String> requestTask = new ArrayList<>();
        requestTask.add(body);
        requestTask.add(idNewTask);
        return requestTask;
    }
}
