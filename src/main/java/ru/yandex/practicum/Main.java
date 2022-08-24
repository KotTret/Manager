package ru.yandex.practicum;

import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.domain.Task;
import ru.yandex.practicum.management.task.Managers;
import ru.yandex.practicum.management.task.TaskManager;

import java.util.TreeSet;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("One", "descr", Status.NEW, 30, "11.03.2022 14:00");
        Task task2 = new Task("One", "descr", Status.NEW, 30, "11.03.2022 14:30");
        Epic epic1 = new Epic("One", "descr");
        Epic epic2 = new Epic("two", "descr");
        Epic epic3 = new Epic("One", "descr");
        Subtask subtask1 = new Subtask("One", "descr", Status.IN_PROGRESS, 30, "11.03.2022 15:33", 3);
        Subtask subtask2 = new Subtask("One", "descr", Status.NEW, 30, "11.03.2022 16:33", 3);

        Subtask subtask4 = new Subtask("One", "descr", Status.DONE, 30, "12.03.2022 14:33", 3);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        epic2.setId(3);
        manager.updateTask(epic2);
      //  Subtask subtask3 = new Subtask("One", "descr", Status.NEW, 30, "11.03.2022 16:34", 3);
      //  subtask3.setId(4);
     //   manager.updateTask(subtask3);
        //Task task3 = new Task("One", "descr", Status.NEW, 30, "11.03.2022 14:33");
       // task3.setId(1);
     //   manager.updateTask(task3);

/*        manager.addEpic(epic3);


        manager.addEpic(epic1);
        manager.addEpic(epic2);



        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        manager.deleteSubtaskById(9);
        manager.deleteSubtaskById(8);
        manager.deleteSubtaskById(7);
        manager.deleteSubtaskById(6);*/


        manager.deleteAll();

    }
}
