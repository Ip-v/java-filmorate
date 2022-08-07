package ru.yandex.practicum.filmorate.exceptions;

/**
 * Исключение проверки существования фильма в базе.
 */
public class FilmDoesNotExistException extends RuntimeException {
    public FilmDoesNotExistException(String s) {
        super(s);
    }
}
