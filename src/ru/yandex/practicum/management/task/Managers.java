package ru.yandex.practicum.management;

public class Managers<T> {

    private final T manager;

    public Managers(T manager) {
        this.manager = manager;
    }

    public T getDefault() {
        return manager;
    }
}
