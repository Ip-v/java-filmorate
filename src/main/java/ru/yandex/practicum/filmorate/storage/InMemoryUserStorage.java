package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//TODO Создайте классы InMemoryFilmStorage и InMemoryUserStorage, имплементирующие новые интерфейсы, и перенесите туда всю логику хранения, обновления и поиска объектов.
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> storage = new HashMap<>();
    int id = 0;

    @Override
    public User save(User user) {
        user.setId(++id);
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if(!storage.containsKey(user.getId())) {
            throw new UserDoesNotExistsException("Пользователя не существует.");
        }
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
        if(!storage.containsKey(id)) {
            throw new UserDoesNotExistsException("Пользователя не существует.");
        }
        storage.remove(id);
    }
}
