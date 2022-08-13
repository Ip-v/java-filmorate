package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.MpaDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.CatalogDBStorage;

import java.util.List;

/**
 * Сервис MPA
 */
@Service
@RequiredArgsConstructor
public class MpaService {
    @Autowired
    private final CatalogDBStorage<Mpa> storage;

    /**
     * Метод возвращает список рейтингов МРА.
     */
    public List<Mpa> getAll() {
        return storage.getAll();
    }

    /**
     * Метод возвращает рейтинг МРА по id.
     */
    public Mpa getById(Integer id) {
        Mpa m = storage.get(id);
        if (m == null) {
            throw new MpaDoesNotExistException("MPA с id = " + id + " не найден в БД.");
        }
        return m;
    }
}