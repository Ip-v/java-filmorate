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
    Film save(@RequestBody Film film);

    /**
     * Обновить объект в коллекции.
     */
    Film update(Film film);

    /**
     * Метод возвращает все элементы коллекции.
     */
    List<Film> getAll();

    /**
     * Получения фильма из базы
     *
     * @param id Ид фильма
     * @return фильм
     */
    Film get(int id);

    /**
     * Удаление фильма из базы по ID.
     */
    void delete(int id);

}
