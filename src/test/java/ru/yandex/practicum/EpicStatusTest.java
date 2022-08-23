package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.management.task.Managers;
import ru.yandex.practicum.management.task.TaskManager;

import static org.junit.jupiter.api.Assertions.*;


class EpicStatusTest {

    private  TaskManager manager;
    private Epic epic;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
        epic = new Epic("One", "learn english");
    }

    private void createAndAddSubtasks(Status  s1, Status s2, Status s3) {
        subtask1 = new Subtask("Qwerty", "qwerty", s1, 30, "12.02.2022 12:33", 1);
        subtask2 = new Subtask("qaz", "qwerty", s2, 30, "12.02.2022 13:33", 1);
        subtask3 = new Subtask("asd", "qwerty", s3, 30, "12.02.2022 14:33", 1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
    }

    @Test
    public void shouldReturnNewStatusForEmptyListOfSubtasks() {
        Status expected = Status.NEW;
        manager.addEpic(epic);
        assertEquals(expected, epic.getStatus());
    }

    @Test
    public void shouldReturnNewStatusForSubtasksWithStatusNew() {
        Status expected = Status.NEW;
        manager.addEpic(epic);
        createAndAddSubtasks(Status.NEW, Status.NEW, Status.NEW);
        assertEquals(expected, epic.getStatus());
    }

    @Test
    public void shouldReturnDoneStatusForSubtasksWithStatusDone() {
        Status expected = Status.DONE;
        manager.addEpic(epic);
        createAndAddSubtasks(Status.DONE, Status.DONE, Status.DONE);
        assertEquals(expected, epic.getStatus());
    }

    @Test
    public void shouldReturnDoneStatusForSubtasksWithStatusNewAndDone() {
        Status expected = Status.IN_PROGRESS;
        manager.addEpic(epic);
        createAndAddSubtasks(Status.DONE, Status.NEW, Status.DONE);
        assertEquals(expected, epic.getStatus());
    }

    @Test
    public void shouldReturnInProgressStatusForSubtasksWithStatusInProgress() {
        Status expected = Status.IN_PROGRESS;
        manager.addEpic(epic);
        createAndAddSubtasks(Status.IN_PROGRESS, Status.IN_PROGRESS, Status.IN_PROGRESS);
        assertEquals(expected, epic.getStatus());
    }

    @Test
    public void shouldReturnInProgressStatusWhenOneSubtaskDeleted() {
        Status expected = Status.DONE;
        manager.addEpic(epic);
        createAndAddSubtasks(Status.DONE, Status.NEW, Status.DONE);
        manager.deleteSubtaskById(3);
        assertEquals(expected, epic.getStatus());
    }

    @Test
    public void shouldReturnInProgressStatusWhenSubtaskUpdate() {
        Status expected = Status.DONE;
        manager.addEpic(epic);
        createAndAddSubtasks(Status.DONE, Status.NEW, Status.DONE);
        subtask2 = new Subtask("Qwerty", "qwerty", Status.DONE, 30, "12.02.2022 13:33", 1);
        subtask2.setId(3);
        manager.updateTask(subtask2);
        assertEquals(expected, epic.getStatus());
    }
}