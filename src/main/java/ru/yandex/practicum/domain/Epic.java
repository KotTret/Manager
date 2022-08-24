package ru.yandex.practicum.domain;

import ru.yandex.practicum.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task implements Comparable<Task> {

    private  List<Integer> listIdSubtask = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, Status.NEW, 0, null);
    }

    public List<Integer> getListIdSubtask() {
        return listIdSubtask;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setListIdSubtask(List<Integer> listIdSubtask) {
        this.listIdSubtask = listIdSubtask;
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
