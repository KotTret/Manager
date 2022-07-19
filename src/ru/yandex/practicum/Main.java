package ru.yandex.practicum;

import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.management.task.Managers;
import ru.yandex.practicum.management.task.TaskManager;


public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();
        // создаём простую задачу
        Task task1 = new Task("1", "buy a book on Java", "NEW");
        Task task2 = new Task("2", "buy a book on Java", "DONE");
        manager.addTask(task1);
        manager.addTask(task2);


        // создаём сложную задачу, пока без подзадач
        Epic task3 = new Epic("3", "learn english");
        // создаём сложные задачи
        Epic task4 = new Epic("4", "understand the task of this sprint");

        Subtask subtask1 = new Subtask("5", "qwerty", "NEW", 3);
        Subtask subtask2 = new Subtask("6", "qwerty", "IN_PROGRESS", 3);
        Subtask subtask3 = new Subtask("7", "qwerty", "NEW", 3);
        Subtask subtask4 = new Subtask("8", "qwerty", "DONE", 4);
        Subtask subtask5 = new Subtask("9", "qwerty", "DONE", 4);

        manager.addEpic(task3);
        manager.addEpic(task4);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        manager.addSubtask(subtask5);
        manager.getTaskById(1);
        manager.getTaskById(2);
        System.out.println(manager.getHistory());
        System.out.println(manager.getHistory().size());
        manager.getTaskById(1);
        manager.getEpicById(3);
       manager.getTaskById(2);
        manager.getTaskById(1);
        manager.getSubtaskById(5);
        manager.getSubtaskById(6);
        manager.deleteTaskById(1);
        System.out.println(manager.getHistory());
        System.out.println(manager.getHistory().size());
        manager.deleteEpicById(3);
        //manager.deleteSubtaskById(5);
        System.out.println(manager.getHistory());
        System.out.println(manager.getHistory().size());
        //System.out.println(manager.getAllSubtaskOfEpic(3));
    }
}

