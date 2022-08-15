package ru.yandex.practicum.filmorate.exceptions;

/**
 * Исключение генерируемое при отсутствии жанра в БД
 */
public class GenreDoesNotExistException extends RuntimeException {
    public GenreDoesNotExistException(String s) {
        super(s);
    }
}
