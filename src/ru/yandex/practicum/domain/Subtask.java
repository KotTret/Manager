package ru.yandex.practicum.domain;

public class Subtask extends Task {
    private String nameEpic;

    public Subtask(String name, String description, String status) {
        super(name, description, status);
    }

    public String getNameEpic() {
        return nameEpic;
    }

    public void setNameEpic(String nameEpic) {
        this.nameEpic = nameEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "nameEpic='" + nameEpic + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id +
                '}';
    }
}
