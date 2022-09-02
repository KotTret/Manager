package ru.yandex.practicum.domain;

import ru.yandex.practicum.Status;

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

    public void setListIdSubtask(List<Integer> listIdSubtask) {
        this.listIdSubtask = listIdSubtask;
    }

    @Override
    public String getEndTime() {
        if (endTime == null) {
            return "Время ещё не задано";
        } else {
            return startTime.plusMinutes(duration.toMinutes()).format(formatter);
        }
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
                "," + getStartTime() + "," + duration.toMinutes() + "," + getEndTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;

        Epic epic = (Epic) o;

        if (!listIdSubtask.equals(epic.listIdSubtask)) return false;
        return getEndTime().equals(epic.getEndTime());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + listIdSubtask.hashCode();
        result = 31 * result + endTime.hashCode();
        return result;
    }
}
