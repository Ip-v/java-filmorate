package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.util.Map;

/**
 * Обработчик ошибок.
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, Integer>> handleUserDoesNotExistException(final UserDoesNotExistException e) {
        log.info("404 {}", e.getMessage());
        return new ResponseEntity<>(Map.of(e.getMessage(), 404),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Integer>> handleValidationError(final ValidationException e) {
        log.info("400 {}", e.getMessage());
        return new ResponseEntity<>(Map.of(e.getMessage(), 400),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Integer>> handleFilmDoesNotExistException(final FilmDoesNotExistException e) {
        log.info("404 {}", e.getMessage());
        return new ResponseEntity<>(Map.of(e.getMessage(), 404),
                HttpStatus.NOT_FOUND);
    }

}
