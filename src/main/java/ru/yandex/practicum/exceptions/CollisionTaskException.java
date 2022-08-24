package ru.yandex.practicum.exceptions;

public class CollisionTaskException extends RuntimeException{

    public CollisionTaskException(String message) {
        super(message);
    }

    public CollisionTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
