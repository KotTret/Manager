package ru.yandex.practicum.domain;

import ru.yandex.practicum.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> listIdSubtask = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, "NEW");
    }

    public List<Integer> getListIdSubtask() {
        return listIdSubtask;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return id + "," + "Epic" + "," + name + "," + status + "," + description + "," + " ";
    }
/*    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id;

        result = result + ", size_listSubtask=" + listIdSubtask.size() + '}';
        return result;
    }*/
}
