package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Хранилище данных о пользователях в памяти.
 */
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> storage = new HashMap<>();
    private int userId = 0;

    @Override
    public User save(User user) {
        user.setId(++userId);
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(int id) {
        return storage.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}
