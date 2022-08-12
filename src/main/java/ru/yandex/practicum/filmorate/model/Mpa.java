package ru.yandex.practicum.filmorate.model;

import lombok.*;

/**
 * Рейтинг Ассоциации кинокомпаний (англ. Motion Picture Association)
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {

    private Integer id;

    @Setter
    private String name;
}