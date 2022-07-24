package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс ошибки
 */
@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
