package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс фильм
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Integer id;

    @NotBlank(message = "Название не должно быть пустым.")
    private String name;

    @Size(max = 200)
    private String description;

    private LocalDate releaseDate;

    @Positive
    private Integer duration;

    private Set<Integer> likes = new HashSet<>();

    private Set<Genre> genres;

    @NotNull
    private Mpa mpa;
}
