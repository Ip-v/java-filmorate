package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Хранилище данных о фильме в памяти.
 */
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> storage = new HashMap<>();
    int filmId = 0;

    @Override
    public Film save(Film film) {
        film.setId(++filmId);
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Film get(int id) {
        return storage.get(id);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}
