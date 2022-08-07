package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Интерфейс контроллера
 *
 * @param <T>
 */
public interface Controller<T> {
    /**
     * Добавить объект в коллекцию
     *
     * @param t Новый объект
     */

    T save(@RequestBody T t);

    /**
     * Обновить объект в коллекции.
     *
     * @param t Новая версия
     */
    T update(T t);

    /**
     * Метод возвращает все элементы коллекции.
     */
    List<T> getAll();

    /**
     * Валидация объекта
     *
     * @param t Объект
     */
    void validate(T t);
}
