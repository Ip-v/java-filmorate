package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Контроллер для обслуживания базы фильмов
 */
@Slf4j
@RestController
@RequestMapping("films")
public class FilmController implements Controller<Film> {
    private final Map<Integer, Film> storage = new HashMap<>();
    private int lastId = 1;

    @Override
    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        validate(film);
        film.setId(lastId);
        lastId++;
        storage.put(film.getId(), film);
        log.info("Добавлен фильм: " + film);
        return film;
    }

    @Override
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (storage.containsKey(film.getId())) {
            validate(film);
            storage.put(film.getId(), film);
            log.info("Обновлена запись фильма id: " + film.getId());
            return film;
        }
        log.error("Ошибка обновления фильма с id: " + film.getId());
        throw new RuntimeException("Ошибка обновления фильма с id: " + film.getId());
    }

    @Override
    @GetMapping
    public List<Film> getAll() {
        log.info("Получен запрос списка всех фильмов в базе.");
        return new ArrayList<>(storage.values());
    }

    @Override
    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            String msg = "Ошибка валидации фильма. Неверная дата релиза. " + film.getId();
            log.warn(msg);
            throw new ValidationException(msg);
        }
        if (film.getDescription().length() > 200) {
            String msg = "Ошибка валидации фильма. Длина описания превышает 200 символов. " + film.getId();
            log.warn(msg);
            throw new ValidationException();
        }
        if (film.getDuration() < 0) {
            String msg = "Ошибка валидации фильма. Отрицательная продолжительность фильма. " + film.getId();
            log.warn(msg);
            throw new ValidationException();
        }
        if (film.getName() == null || film.getName().isBlank()) {
            String msg = "Ошибка валидации фильма. Название фильма пустое. " + film.getId();
            log.warn(msg);
            throw new ValidationException();
        }
        log.trace("Успешная валидация фильма " + film);
    }
}
