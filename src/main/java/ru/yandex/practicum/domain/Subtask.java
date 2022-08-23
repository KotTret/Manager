package ru.yandex.practicum.domain;

import ru.yandex.practicum.Status;

public class Subtask extends Task implements Comparable<Task> {
    private Integer idEpic;

    public Subtask(String name, String description, Status status, int duration, String startTime, Integer idEpic) {
        super(name, description, status, duration, startTime);
        this.idEpic = idEpic;
    }

    public Integer getIdEpic() {
        return idEpic;
    }


    @Override
    public String toString() {
        return id + "," + "SUBTASK" + "," + name + "," + status + "," + description + "," + idEpic + "," +
                getStartTime() + ","  + duration.toMinutes();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subtask)) return false;
        if (!super.equals(o)) return false;

        Subtask subtask = (Subtask) o;

        return idEpic.equals(subtask.idEpic);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + idEpic.hashCode();
        return result;
    }
}
