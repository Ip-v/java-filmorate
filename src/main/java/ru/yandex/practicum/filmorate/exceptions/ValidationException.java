package ru.yandex.practicum.filmorate.exceptions;

/**
 * Генерирует исключения при неверном формате данных
 */
public class ValidationException extends RuntimeException {
    public ValidationException() {
        super("Неверный формат");
    }

    public ValidationException(String message) {
        super(message);
    }
}
