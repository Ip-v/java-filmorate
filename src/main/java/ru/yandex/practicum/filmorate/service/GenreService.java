package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.GenreDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.CatalogDBStorage;

import java.util.List;

/**
 * Сервис жанров
 */
@Service
@RequiredArgsConstructor
public class GenreService {
    @Autowired
    private final CatalogDBStorage<Genre> storage;

    /**
     * Возвращает список жанорм из справочника.
     */
    public List<Genre> getAll() {
        return storage.getAll();
    }

    /**
     * Возвращает жанор по id из справочника.
     */
    public Genre getById(int id) {
        Genre g = storage.get(id);
        if (g == null) {
            throw new GenreDoesNotExistException("Жанр с id = " + id + " не найден в БД.");
        }
        return g;
    }
}
