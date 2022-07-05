package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmControllerTest {
    Controller<Film> controller;
    Film film;

    @BeforeEach
    void init() {
        controller = new FilmController();
        film = new Film(1, "Name", "Description", LocalDate.now(), 120);
    }

    @Test
    void shouldValidateCorectFilm() {
        film = new Film(1, "Name", "Description", LocalDate.now(), 120);
        assertTrue(controller.validate(film));
    }

    @Test
    void shouldValidateFilmWithEdgeReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        assertTrue(controller.validate(film), "Ошибка проверки граничного условия.");
    }

    @Test
    void shouldNotValidateFilmWithBeforeEdgeReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }

    @Test
    void durationShouldBePositive() {
        film.setDuration(0);
        assertTrue(controller.validate(film));
        film.setDuration(-2);
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }

    @Test
    void descriptionShouldHaveCorrectLength() {
        film.setDescription("sa");
        assertTrue(controller.validate(film));


        String s = "i".repeat(250);
        film.setDescription(s);
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }

    @Test
    void nameShouldNotBeEmptyOrBlank() {
        film.setName("sa");
        assertTrue(controller.validate(film));

        film.setName("");
        assertThrows(ValidationException.class, () -> controller.validate(film));

        film.setName(null);
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }
}