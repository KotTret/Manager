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
        Epic task2 = new Epic("Study", "learn english");
        // создаём сложные задачи
        Epic task3 = new Epic("Understand", "understand the task of this sprint");

        Subtask subtask1 = new Subtask("One", "qwerty", "NEW", 2);
        Subtask subtask2 = new Subtask("Two", "qwerty", "IN_PROGRESS", 2);
        Subtask subtask3 = new Subtask("One", "qwerty", "NEW", 2);
        Subtask subtask4 = new Subtask("Two", "qwerty", "DONE", 3);
        Subtask subtask5 = new Subtask("three", "qwerty", "IN_PROGRESS", 3);

        manager.addTask(task1);
        manager.addEpic(task2);
        manager.addEpic(task3);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        manager.addSubtask(subtask5);

        System.out.println("Получим список всех задач:");
        check(manager);

        System.out.println("Получение задачи по идентификатору:");
        System.out.println(manager.getEpicById(2) + "\n");

        System.out.println("Обновление задачи по идентификатору:");
        Subtask subtask = new Subtask("olololo", "qwerty", "IN_PROGRESS", 3);
        subtask.setId(8);
        manager.updateTask(subtask);
        check(manager);
/*
        System.out.println("Когда удалим эпик:");
        manager.deleteEpicById(2);
        check(manager);*/

        System.out.println("Когда удалим subtask:");
        manager.deleteSubtaskById(5);
        check(manager);

    }

    public static void check(Manager manager) {
        for (Integer i : manager.getTaskList().keySet()) {
            System.out.println(manager.getTaskList().get(i));
        }
        System.out.println();

        for (Integer i : manager.getEpicList().keySet()) {
            System.out.println(manager.getEpicList().get(i));
        }
        System.out.println();

        for (Integer i : manager.getSubtaskList().keySet()) {
            System.out.println(manager.getSubtaskList().get(i));
        }
        System.out.println();
    }
}

