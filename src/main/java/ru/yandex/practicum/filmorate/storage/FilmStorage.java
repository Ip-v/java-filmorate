package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

/**
 * Содержит методы добавления, удаления и модификации объектов фильмы.
 */
public interface FilmStorage {
    /**
     * Добавить объект в коллекцию
     */
    Film add(@RequestBody Film film);

    /**
     * Обновить объект в коллекции.
     */
    Film update(Film film);

    /**
     * Метод возвращает все элементы коллекции.
     */
    List<Film> getAll();

    /**
     * Валидация объекта
     *
     */
    void validate(Film film);
}
