package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Служба фильмов
 */
@Service
public class FilmService {
    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private UserService userService;

    /**
     * Добавление фильма
     */
    public Film save(Film film) {
        return filmStorage.save(film);
    }

    /**
     * Получение фильма по ИД
     */
    public Film get(int filmId) {
        Film film = filmStorage.get(filmId);
        if (film == null) {
            throw new FilmDoesNotExistException("Фильма с ID {" + filmId + "} не существует в базе.");
        }
        return film;
    }

    /**
     * Обновление фильма
     */
    public Film update(Film film) {
        get(film.getId());
        return filmStorage.update(film);
    }

    /**
     * Получение списка всех фильмов
     */
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    /**
     * Добавить лайк фильму от пользователя
     */
    public void likeFilm(int id, int userId) {
        userService.get(userId);
        Film film = get(id);
        film.getUserLikes().add(userId);
    }

    /**
     * Удалить лайк у фильма
     */
    public void deleteFilmLike(int id, int userId) {
        userService.get(userId);
        Film film = get(id);
        film.getUserLikes().remove(userId);
    }

    /**
     * Получить список популярных фильмов
     */
    public List<Film> getPopular(int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparingInt(f -> -1 * f.getUserLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
