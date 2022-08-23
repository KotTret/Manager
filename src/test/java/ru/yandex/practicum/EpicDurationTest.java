package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.domain.Epic;
import ru.yandex.practicum.domain.Subtask;
import ru.yandex.practicum.management.task.Managers;
import ru.yandex.practicum.management.task.TaskManager;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;


public class EpicDurationTest {
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
        subtask2 = new Subtask("qaz", "qwerty", s2, 40, "12.02.2022 13:33", 1);
        subtask3 = new Subtask("asd", "qwerty", s3, 50, "12.02.2022 14:33", 1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
    }

    @Test
    public void shouldReturnZeroDurationForEmptyListOfSubtasks() {
        Duration expected = Duration.ofMinutes(0);
        manager.addEpic(epic);
        assertEquals(expected, epic.getDuration());
    }

    @Test
    public void shouldReturnDurationWhenOneSubtask() {
        Duration expected = Duration.ofMinutes(30);
        manager.addEpic(epic);
        subtask1 = new Subtask("Qwerty", "qwerty", Status.DONE, 30, "12.02.2022 12:33", 1);
        manager.addSubtask(subtask1);
        assertEquals(expected, epic.getDuration());
    }

    @Test
    public void shouldReturnDurationWhenSeveralSubtasks() {
        Duration expected = Duration.ofMinutes(170);
        manager.addEpic(epic);
        createAndAddSubtasks(Status.DONE, Status.NEW, Status.DONE);
        assertEquals(expected, epic.getDuration());
    }

    @Test
    public void shouldReturnDurationWhenSubtaskDeletedFromMiddle() {
        Duration expected = Duration.ofMinutes(170);
        manager.addEpic(epic);
        createAndAddSubtasks(Status.DONE, Status.NEW, Status.DONE);
        manager.deleteSubtaskById(3);
        assertEquals(expected, epic.getDuration());
    }

    @Test
    public void shouldReturnDurationWhenSubtaskDeletedFromEnd() {
        Duration expected = Duration.ofMinutes(100);
        manager.addEpic(epic);
        createAndAddSubtasks(Status.DONE, Status.NEW, Status.DONE);
        manager.deleteSubtaskById(4);
        assertEquals(expected, epic.getDuration());
    }

    @Test
    public void shouldReturnDurationWhenSubtaskDeletedFromBeginning() {
        Duration expected = Duration.ofMinutes(110);
        manager.addEpic(epic);
        createAndAddSubtasks(Status.DONE, Status.NEW, Status.DONE);
        manager.deleteSubtaskById(2);
        assertEquals(expected, epic.getDuration());
    }

    @Test
    public void shouldReturnDurationWhenWhenSubtaskUpdate() {
        Duration expected = Duration.ofMinutes(190);
        manager.addEpic(epic);
        createAndAddSubtasks(Status.DONE, Status.NEW, Status.DONE);
        subtask3 = new Subtask("Qwerty", "qwerty", Status.DONE, 60, "12.02.2022 14:43", 1);
        subtask3.setId(4);
        manager.updateTask(subtask3);
        assertEquals(expected, epic.getDuration());
    }
}
