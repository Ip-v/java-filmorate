package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//Рейтинг Ассоциации кинокомпаний (англ. Motion Picture Association)
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Mpa {

    private final int id;

    @Setter
    private String name;
}