package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
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
    @JsonIgnore
    Set<Integer> userLikes = new HashSet<>();
    private Integer id;
    @NotBlank(message = "Название не должно быть пустым.")
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive
    private Integer duration;
}
