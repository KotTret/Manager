package ru.yandex.practicum.domain;

import ru.yandex.practicum.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task implements Comparable<Task> {
    private final List<Integer> listIdSubtask = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW, 0, null);
    }

    public List<Integer> getListIdSubtask() {
        return listIdSubtask;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDuration() {
        if (startTime == null || endTime == null) {
            duration = Duration.ofMinutes(0);
        } else {
            duration = Duration.between(startTime, endTime);
        }
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        if (startTime == null) {
            return;
        }
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return id + "," + "EPIC" + "," + name + "," + status + "," + description + "," + " " +
                "," + getStartTime() + "," + duration.toMinutes();
    }

}
