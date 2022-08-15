package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

/**
 * Содержит методы добавления, удаления и модификации объектов пользователи в хранилище.
 */
public interface UserStorage {
    /**
     * Добавить объект в коллекцию
     */
    User save(User user);

    /**
     * Обновить объект в коллекции.
     */
    User update(User user);

    /**
     * Получение пользователя из хранилища.
     *
     * @param id ИД пользователя.
     */
    User get(Integer id);

    /**
     * Метод возвращает все элементы коллекции.
     */
    List<User> getAll();

    /**
     * Удаление пользователя из Базы
     */
    void delete(int id);

}
