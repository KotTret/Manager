package ru.yandex.practicum.servers.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.management.task.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


abstract class Handler implements HttpHandler {
    protected TaskManager manager;
    protected Gson gson;
    protected String response;
    protected String method;
    protected Headers headers;
    protected int rCode;
    protected String bodyTask;
    protected String idNewTask;


    public Handler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        method = exchange.getRequestMethod();
        headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json");
        rCode = 200;
        bodyTask = checkAndReturnPassedRequestParameters(exchange).get(0);
        idNewTask = checkAndReturnPassedRequestParameters(exchange).get(1);
    }

    private ArrayList<String> checkAndReturnPassedRequestParameters(HttpExchange exchange) throws IOException {
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

    protected void writeResponseBody(HttpExchange exchange) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}
