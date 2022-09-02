package ru.yandex.practicum.clients;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import ru.yandex.practicum.exceptions.CollisionTaskException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    HttpClient client = HttpClient.newHttpClient();
    private final String url;
    private final String apiToken;

    public KVTaskClient(String url) {
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
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (!(response.statusCode() == 200)) {
              throw new CollisionTaskException("Что-то пошло не так. Сервер вернул код состояния: \" + response.statusCode()");
            }
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
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
                JsonElement jsonElement = JsonParser.parseString(response.body());

                return jsonElement.getAsString();

            } else {
               // System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) { // обрабатываем ошибки отправки запроса
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
            return null;
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
            return response.body();
        } catch (IOException | InterruptedException e) { // обработка ошибки отправки запроса
            System.out.println("Во время выполнения запроса ресурса по URL-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
            return null;
        }
    }
}
