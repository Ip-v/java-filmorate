package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * Класс фильм
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Film {
    private Integer id;
    @NotBlank(message = "Название не должно быть пустым.")
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
}
