package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;

    @Test
    void save() {
        Film film = new Film(-1, "New test film2", "Description", LocalDate.now(), 90, new HashSet<>(), new HashSet<>(),
                new Mpa(1, "G"));
        assertTrue(filmDbStorage.save(film).getId() != -1);
        assertThrows(ValidationException.class, () -> filmDbStorage.save(film));
    }

    @Test
    void update() {
        Film film = new Film(1, "new name", "Description", LocalDate.now(), 90, new HashSet<>(), new HashSet<>(),
                new Mpa(1, "G"));
        assertEquals("new name", filmDbStorage.update(film).getName());
    }

    @Test
    void getAll() {
        assertEquals(2, filmDbStorage.getAll().size());
    }

    @Test
    void get() {
        Film film = filmDbStorage.get(1);
        assertThat(film).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void deleteTest() {
        filmDbStorage.delete(1);
        assertNull(filmDbStorage.get(1));
    }

    @Test
    void getPopular() {
        assertEquals(1, filmDbStorage.getPopular(10).get(0).getId());
    }
}