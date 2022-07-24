package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * Контроллер фильмов
 */
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController implements Controller<Film> {
    @Autowired
    private FilmService filmService;

    /**
     * Получение фильма
     * @param id ид фильма
     * @return объект фильм
     */
    @GetMapping("{id}")
    public Film get(@PathVariable int id) {
        log.info("Получен запрос фильма ИД " + id);
        return filmService.get(id);
    }

    @Override
    @GetMapping
    public List<Film> getAll() {
        log.info("Получен запрос списка всех фильмов в базе.");
        return filmService.getAll();
    }

    /**
     * Получение списка ТОП фильмов
     * @param count Кол-во (10 по умолчанию)
     * @return список топ фильмов
     */
    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(required = false, defaultValue = "10") int count) {
        log.info("Получен запрос топ " + count + " популярных фильмов.");
       return filmService.getPopular(count);
    }

    @Override
    @PostMapping
    public Film save(@Valid @RequestBody Film film) {
        log.info("Получен запрос сохранения фильма " + film);
        validate(film);
        return filmService.save(film);
    }

    @Override
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен запрос обновления фильма " + film);
        validate(film);
        return filmService.update(film);
    }

    /**
     * Добавляет лайк фильму
     * @param id ид фильма
     * @param userId ид пользователя
     */
    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь " + userId + " ставит лайк фильму " + id);
        filmService.likeFilm(id, userId);
    }

    /**
     * Удаляет лайк фильма
     * @param id ид фильма
     * @param userId ид пользователя
     */
    @DeleteMapping("{id}/like/{userId}")
    public void deleteFilmLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь " + userId + " удаляет лайк фильму " + id);
        filmService.deleteFilmLike(id, userId);
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
