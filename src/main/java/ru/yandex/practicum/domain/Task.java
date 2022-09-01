package ru.yandex.practicum.domain;
import ru.yandex.practicum.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task implements Comparable<Task> {
    protected String name;
    protected String description;
    protected Status status;
    protected Integer id;
    protected Duration duration;
    protected LocalDateTime startTime;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public Task(String name, String description, Status status, int duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        if (startTime == null) {
            this.startTime = null;
        } else {
            this.startTime = LocalDateTime.parse(startTime, formatter);
        }
    }

    public Duration getDuration() {
        return duration;
    }

    public String getStartTime() {
        if (startTime == null) {
            return "Время ещё не задано";
        } else {
            return startTime.format(formatter);
        }
    }

    public String getEndTime() {
        if (startTime == null) {
            return "Время ещё не задано";
        } else {
            return startTime.plusMinutes(duration.toMinutes()).format(formatter);
        }
    }

    public void setStartTime(LocalDateTime startTime) {
        if (startTime == null) {
            return;
        }
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id + "," + "TASK" + "," + name + "," + status + "," + description + ","
                + " " + "," + getStartTime() + ","  + duration.toMinutes() + "," + getEndTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (!name.equals(task.name)) return false;
        if (!description.equals(task.description)) return false;
        if (status != task.status) return false;
        if (!id.equals(task.id)) return false;
        if (!duration.equals(task.duration)) return false;
        return getStartTime().equals(task.getStartTime());
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + duration.hashCode();
        result = 31 * result + startTime.hashCode();
        return result;
    }

    @Override
    public int compareTo(Task o) {
        if(this.startTime == null && o.startTime == null) {
            return this.id - o.id;
        } else if (o.startTime == null) {
            return -1;
        } else if (this.startTime == null) {
            return 1;
        } else {
            return this.startTime.compareTo(o.startTime);
        }
    }
}
