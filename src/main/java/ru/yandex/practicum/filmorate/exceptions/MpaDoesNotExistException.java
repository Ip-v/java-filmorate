package ru.yandex.practicum.filmorate.exceptions;

/**
 * Исключение поиска Mpa
 */
public class MpaDoesNotExistException extends RuntimeException {
    public MpaDoesNotExistException(String message) {
        super(message);
    }
}
