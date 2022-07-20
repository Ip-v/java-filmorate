package ru.yandex.practicum.filmorate.storage;

public class UserDoesNotExistsException extends RuntimeException {
    public UserDoesNotExistsException(String msg) {
        super(msg);
    }
}
