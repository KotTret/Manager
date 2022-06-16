package ru.yandex.practicum.domain;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> listIdSubtask = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, "NEW");
    }

    public List<Integer> getListIdSubtask() {
        return listIdSubtask;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id;

        if (listIdSubtask != null) {
            result = result + ", size_listSubtask=" + listIdSubtask.size() + '}';
            ;
        } else {
            result = result + ", listSubtask=null" + '}';
        }
        return result;
    }
}