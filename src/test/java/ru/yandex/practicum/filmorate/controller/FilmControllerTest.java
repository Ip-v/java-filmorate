package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    Controller<Film> controller;
    Film film;

    @BeforeEach
    void init() {
        controller = new FilmController();
        film = new Film(1, "Name", "Description", LocalDate.now(), 120, null, null, null);
    }

    @Test
    void shouldValidateCorectFilm() {
        film = new Film(1, "Name", "Description", LocalDate.now(), 120, null, null, null);
        assertDoesNotThrow(() -> controller.validate(film));
    }

    @Test
    void shouldValidateFilmWithEdgeReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        assertDoesNotThrow(() -> controller.validate(film), "Ошибка проверки граничного условия.");
    }

    @Test
    void shouldNotValidateFilmWithBeforeEdgeReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }

    @Test
    void durationShouldBePositive() {
        film.setDuration(0);
        assertDoesNotThrow(() -> controller.validate(film));

        film.setDuration(-2);
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }

    @Test
    void descriptionShouldHaveCorrectLength() {
        film.setDescription("sa");
        assertDoesNotThrow(() -> controller.validate(film));


        String s = "i".repeat(250);
        film.setDescription(s);
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }

    @Test
    void nameShouldNotBeEmptyOrBlank() {
        film.setName("sa");
        assertDoesNotThrow(() -> controller.validate(film));

        film.setName("");
        assertThrows(ValidationException.class, () -> controller.validate(film));

        film.setName(null);
        assertThrows(ValidationException.class, () -> controller.validate(film));
    }
}