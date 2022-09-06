package ru.yandex.practicum.clients;

import com.google.gson.JsonParser;
import ru.yandex.practicum.exceptions.ConnectionToServerException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client;
    private final String url;
    private final String apiToken;

    public KVTaskClient(String url) {
        client = HttpClient.newHttpClient();
        this.url = url;
        apiToken = register(url);
    }

    public void put(String key, String json) {
        URI urlSave = URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(urlSave)
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            if (!(response.statusCode() == 200)) {
              throw new ConnectionToServerException("Что-то пошло не так. Сервер вернул код состояния: "
                      + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            String message = "Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.";
            throw new ConnectionToServerException(message, e);
        }
    }

    public String load(String key) {
        URI urlLoad = URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(urlLoad)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return JsonParser.parseString(response.body()).getAsString();
            } else {
               String message = "Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode();
               throw new ConnectionToServerException(message);
            }
        } catch (IOException | InterruptedException e) {
            String message = "Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.";
            throw new ConnectionToServerException(message, e);
        }
    }

    private String register(String url) {
        URI urlRegister = URI.create(url + "register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(urlRegister)
                .header("Accept", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
               if (response.statusCode() == 200) {
                return response.body();
            } else {
               String message = "Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode();
               throw new ConnectionToServerException(message);
            }
        } catch (IOException | InterruptedException e) {
             String message = "Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.";
            throw new ConnectionToServerException(message, e);
        }
    }
}
