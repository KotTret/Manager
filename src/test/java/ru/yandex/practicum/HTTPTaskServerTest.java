package ru.yandex.practicum;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.management.task.TaskManager;
import ru.yandex.practicum.servers.HttpTaskServer;
import ru.yandex.practicum.servers.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HTTPTaskServerTest {
    private final String urlKVServer = "http://localhost:8078/";
    private final String urlTaskServer = "http://localhost:8080/";
    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;
    private HttpClient client;
    private static Gson gson;
    private TaskManager manager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask1;
    private Subtask subtask2;
    private Subtask subtask3;

    @BeforeEach
     void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.createAndStartServer();
        client = HttpClient.newHttpClient();
        manager = HttpTaskServer.manager;
        task1 = new Task("one", "qwerty", Status.DONE, 30, "13.03.2022 12:33");
        task2 = new Task("one", "qwerty", Status.DONE, 30, "14.02.2022 12:33");
        epic1 = new Epic("One", "learn english");
        epic2 = new Epic("One", "learn english");
        subtask1 = new Subtask("Qwerty", "qwerty", Status.DONE, 30, "12.02.2022 12:33", 3);
        subtask2 = new Subtask("qazd3", "qwerty", Status.NEW, 40, "12.02.2022 13:33", 3);
        subtask3 = new Subtask("asd", "qwerty", Status.IN_PROGRESS, 50, "12.02.2022 11:33", 4);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
    }


    @AfterEach
      void afterEach() {
        HttpTaskServer.manager.deleteAll();
        kvServer.stop();
        httpTaskServer.stop();

    }

    @BeforeAll
     static void beforeAll() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    @Test
    void checkAllTasksHandlerWhenGETRequest() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        List<Map<Integer, ? extends Task>> allTasks = List.of(manager.getTasks(), manager.getSubtasks(),
                manager.getEpics());
        String expected = gson.toJson(allTasks);

        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void checkAllTasksHandlerWhenDELETERequest() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE()
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        List<Map<Integer, ? extends Task>> allTasks = List.of(manager.getTasks(), manager.getSubtasks(),
                manager.getEpics());
        String expected = gson.toJson(allTasks);

        assertEquals(expected, actual, "Удаление не произошло");
    }

    @Test
    void checkTasksHandlerWhenGETRequestAllTasks() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getTasks());

        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void checkTasksHandlerWhenGETRequestById() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getTaskById(1));

        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void checkTasksHandlerWhenDELETERequestAllTasks() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE()
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getTasks());

        assertTrue(manager.getTasks().isEmpty(), "Удаление не произошло");
        assertEquals(expected, actual, "Удаление не произошло");
    }

    @Test
    void checkTasksHandlerWhenDELETERequestById() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/task?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE()
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(1, manager.getTasks().size(), "Удаление не произошло");
    }

    @Test
    void checkTasksHandlerWhenPOSTRequestToAdd() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/task");
        Task newTask = new Task("ololo", "qwerty", Status.DONE, 30, "16.03.2022 12:33");
        String newTaskJson = gson.toJson(newTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(newTaskJson))
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(3, manager.getTasks().size(), "Задача не добавлена");
    }

    @Test
    void checkTasksHandlerWhenPOSTRequestToUpdate() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/task?id=1");
        Task newTask = new Task("ololo", "qwerty", Status.DONE, 30, "16.02.2022 12:33");
        newTask.setId(1);
        String newTaskJson = gson.toJson(newTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(newTaskJson))
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Task actual = manager.getTaskById(1);
        assertEquals(newTask, actual, "Задача не обновлена");
        assertEquals(2, manager.getTasks().size(), "Задача не обновлена");
    }

    @Test
    void checkSubtasksHandlerWhenGETRequestAllSubtask() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getSubtasks());

        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void checkSubtasksHandlerWhenGETRequestById() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/subtask?id=5");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getSubtaskById(5));

        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void checkSubtasksHandlerWhenDELETERequestAllSubtasks() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE()
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getSubtasks());

        assertTrue(manager.getSubtasks().isEmpty(), "Удаление не произошло");
        assertEquals(expected, actual, "Удаление не произошло");
    }

    @Test
    void checkSubtasksHandlerWhenDELETERequestById() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/subtask?id=6");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE()
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(2, manager.getSubtasks().size(), "Удаление не произошло");
    }

    @Test
    void checkSubtasksHandlerWhenPOSTRequestToAdd() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/subtask");
        Subtask newSubtask = new Subtask("okoko", "qwerty", Status.DONE, 30, "12.04.2022 12:33", 4);
        String newTaskJson = gson.toJson(newSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(newTaskJson))
                .header("Content-Type", "application/json").build();
      client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(4, manager.getSubtasks().size(), "Задача не добавлена");
    }

    @Test
    void checkSubtasksHandlerWhenPOSTRequestToUpdate() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/subtask?id=5");
        Subtask newSubtask = new Subtask("okoko", "qwerty", Status.DONE, 30, "12.02.2022 12:33", 4);
        newSubtask.setId(5);
        String newTaskJson = gson.toJson(newSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(newTaskJson))
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Task actual = manager.getSubtaskById(5);
        assertEquals(newSubtask, actual, "Задача не обновлена");
        assertEquals(3, manager.getSubtasks().size(), "Задача не обновлена");
    }

    @Test
    void checkEpicsHandlerWhenGETRequestAllEpics() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getEpics());

        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void checkEpicsHandlerWhenGETRequestById() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/epic?id=4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getEpicById(4));

        assertEquals(expected, actual, "Получен не верный Json с задачами");
    }

    @Test
    void checkEpicsHandlerWhenDELETERequestAllEpics() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE()
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getEpics());

        assertTrue(manager.getEpics().isEmpty(), "Удаление не произошло");
        assertEquals(expected, actual, "Удаление не произошло");
    }

    @Test
    void checkEpicsHandlerWhenDELETERequestById() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/epic?id=4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE()
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(1, manager.getEpics().size(), "Удаление не произошло");
    }

    @Test
    void checkEpicsHandlerWhenPOSTRequestToAdd() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/epic");
        Epic newEpic = new Epic("olololo", "qwerty");
        String newTaskJson = gson.toJson(newEpic);
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(newTaskJson))
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(3, manager.getEpics().size(), "Задача не добавлена");
    }

    @Test
    void checEpicsHandlerWhenPOSTRequestToUpdate() throws IOException, InterruptedException {
        URI url = URI.create(urlTaskServer + "tasks/epic?id=3");
        Epic newEpic = new Epic("olololo", "qwerty");
        newEpic.setId(3);
        Epic oldEpic = manager.getEpicById(3);
        String newTaskJson = gson.toJson(newEpic);
        HttpRequest request = HttpRequest.newBuilder().uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(newTaskJson))
                .header("Content-Type", "application/json").build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Task actual = manager.getEpicById(3);
        assertNotEquals(oldEpic, actual, "Задача не обновлена");
        assertEquals(2, manager.getEpics().size(), "Задача не обновлена");
    }

    @Test
    void checkHistoryHandlerWhenGETRequest() throws IOException, InterruptedException {
        manager.getTaskById(1);
        manager.getEpicById(3);
        URI url = URI.create(urlTaskServer + "tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET()
                .header("Content-Type", "application/json").build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        String actual = gson.toJson(jsonElement);

        String expected = gson.toJson(manager.getHistory());

        assertEquals(2, manager.getHistory().size());
        assertEquals(expected, actual, "Получен не верный Json с историей");
    }
}
