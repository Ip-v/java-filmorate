package ru.yandex.practicum.filmorate.exceptions;

/**
 * Исключение проверки существования пользователя в базе.
 */
public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(String s) {
        super(s);
    }
}
