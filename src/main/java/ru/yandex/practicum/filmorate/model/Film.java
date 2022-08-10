package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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
    @JsonIgnore
    private Set<Integer> userLikes = new HashSet<>();
    @JsonIgnore
    private Set<Genre> genreList;
    @NotNull
    private Mpa mpa;
}
