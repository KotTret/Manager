package ru.yandex.practicum.domain;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> listSubtask;
    private int idSub = 0;

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, List<Subtask> listSubtask) {
        super(name, description);
        this.status = null;
        this.listSubtask = listSubtask;
        for (Subtask s : listSubtask) {
            s.setId(idSub);
            s.setNameEpic(this.name);
            if (s.getStatus().equals("DONE") && (this.status == null || this.status.equals("DONE"))) {
                this.status = "DONE";
            } else if (s.getStatus().equals("NEW") && (this.status == null || this.status.equals("NEW"))) {
                this.status = "NEW";
            } else {
                this.status = "IN_PROGRESS";
            }
            idSub++;
        }
    }

    public List<Subtask> getListSubtask() {
        return listSubtask;
    }

    public void setListSubtask(Subtask subtask) {
        subtask.setId(id);
        if (listSubtask == null) {
            listSubtask = new ArrayList<>();
            listSubtask.add(subtask);
        } else {
            listSubtask.add(subtask);
        }
    }


    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id;

        if (listSubtask != null) {
            result = result + ", size_listSubtask=" + listSubtask.size() + '}';
            ;
        } else {
            result = result + ", listSubtask=null" + '}';
        }
        return result;
    }
}
