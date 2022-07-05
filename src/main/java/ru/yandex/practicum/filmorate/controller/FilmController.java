package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Контроллер для обслуживания базы фильмов
 */
@RestController
@RequestMapping("films")
public class FilmController implements Controller<Film> {
    private final HashMap<Integer, Film> storage = new HashMap<>();
    private int lastId = 1;


    @Override
    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        validate(film);
        film.setId(lastId);
        lastId++;
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (storage.containsKey(film.getId())) {
            validate(film);
            storage.put(film.getId(), film);
            return film;
        }
        throw new RuntimeException("Фильма нет в базе. Невозможно обновить.");
    }

    @Override
    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException();
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException();
        }
        if (film.getDuration() < 0) {
            throw new ValidationException();
        }
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException();
        }
        return true;
    }
}
