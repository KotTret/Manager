package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.management.task.InMemoryTaskManager;
import ru.yandex.practicum.management.task.Managers;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{

    @BeforeEach
    void beforeEach() {
        manager = Managers.getDefault();
    }
}
