package ru.yandex.practicum.exceptions;

public class ConnectionToServerException extends RuntimeException {

    public ConnectionToServerException() {
    }

    public ConnectionToServerException(String message) {
        super(message);
    }

    public ConnectionToServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
