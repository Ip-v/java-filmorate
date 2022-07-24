package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Служба пользователей
 */
@Service
public class UserService {
    @Autowired
    UserStorage userStorage;

    /**
     * Получение пользователя по ИД
     */
    public User get(int id) {
        final User user = userStorage.get(id);
        if (user == null) {
            throw new UserDoesNotExistException("Пользователя с ID {" + id + "} не существует в базе.");
        }
        return userStorage.get(id);
    }

    /**
     * Сохранение пользователя в хранилище
     */
    public User save(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.save(user);
    }

    /**
     * Добавление друга
     */
    public void addFriend(User user, User friend) {
        user.getFriendsIds().add(friend.getId());
        friend.getFriendsIds().add(user.getId());
    }

    /**
     * Удаление друга
     */
    public void removeFriend(User user, User friend) {
        user.getFriendsIds().remove(friend.getId());
        friend.getFriendsIds().remove(user.getId());
    }

    /**
     * Обновление пользователя
     */
    public User update(User user) {
        get(user.getId());
        return userStorage.update(user);
    }

    /**
     * Получение всех пользователей из хранилища
     */
    public List<User> getAll() {
        return userStorage.getAll();
    }

    /**
     * Получение списка друзей
     */
    public List<User> getFriends(User user) {
        Set<Integer> friendsIds = user.getFriendsIds();
        List<User> friends = new ArrayList<>();
        for (Integer f : friendsIds) {
            friends.add(get(f));
        }
        return friends;
    }

    /**
     * Получение списка общих друзей
     */
    public List<User> getCommonFriends(User user, User otherUser) {
        List<User> res = new ArrayList<>();
        user.getFriendsIds().stream()
                .filter(id -> otherUser.getFriendsIds().contains(id))
                .forEach(id -> res.add(get(id)));
        return res;
    }
}
