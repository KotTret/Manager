package ru.yandex.practicum;

import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.management.Manager;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        // создаём простую задачу
        Task task1 = new Task("Buy", "buy a book on Java", "NEW");
        // создаём сложную задачу, пока без подзадач
        Task task2 = new Epic("Study", "learn english");
        // создаём сложные задачи
        Task task3 = createEpic("Understand", "understand the task of this sprint", createListSub1());
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        System.out.println("Получим список всех задач:");
        for (Integer i : manager.getTaskList().keySet()) {
            System.out.println(manager.getTaskList().get(i));
        }
        System.out.println();


        System.out.println("Получение задачи по идентификатору:");
        System.out.println(manager.getTaskById(3) + "\n");

        System.out.println("Обновление задачи по идентификатору:");
        Task task4 = createEpic("Understand", "understand the task of this sprint", createListSub2());
        manager.updateTask(task4, 3);
        System.out.println(manager.getTaskById(3) + "\n");
        System.out.println("Получим список всех задач:");
        for (Integer i : manager.getTaskList().keySet()) {
            System.out.println(manager.getTaskList().get(i));
        }
        System.out.println();

        //удаление по Id
        manager.deleteTasksById(1);

        System.out.println("Получим список всех подзадач определённого эпика:");
        System.out.println(manager.getAllSubtask(3));
        System.out.println();

        // удалим все задачи
        manager.deleteAllTasks();
        try {
            for (Integer i : manager.getTaskList().keySet()) {
                System.out.println(manager.getTaskList().get(i));
            }
        } catch (NullPointerException ex) {
            System.out.println("Список задач пуст");
        }
    }

    public static Epic createEpic(String name, String des, List<Subtask> s) {
        return new Epic(name, des, s);
    }

    public static List<Subtask> createListSub1() {
        Subtask subtask1 = new Subtask("One", "qwerty", "DONE");
        Subtask subtask2 = new Subtask("Two", "qwerty", "IN_PROGRESS");
        List<Subtask> s = new ArrayList<>();
        s.add(subtask1);
        s.add(subtask2);
        return s;
    }

    public static List<Subtask> createListSub2() {
        Subtask subtask1 = new Subtask("One", "qwerty", "DONE");
        Subtask subtask2 = new Subtask("Two", "qwerty", "DONE");
        Subtask subtask3 = new Subtask("three", "qwerty", "DONE");
        List<Subtask> s = new ArrayList<>();
        s.add(subtask1);
        s.add(subtask2);
        s.add(subtask3);
        return s;
    }
}
