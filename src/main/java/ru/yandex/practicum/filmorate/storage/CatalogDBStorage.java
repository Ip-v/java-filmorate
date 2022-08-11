package ru.yandex.practicum.filmorate.storage;

import java.util.List;

/**
 * Интерфейс справочника
 */
public interface CatalogDBStorage<T> {
    /**
     * Получить все записи справочника
     */
    List<T> getAll();

    /**
     * Получить запись по id
     */
    T get(Integer id);
}
