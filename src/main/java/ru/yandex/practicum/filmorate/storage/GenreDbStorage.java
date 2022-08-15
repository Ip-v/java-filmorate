package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Хранилище жанров.
 */
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements CatalogDBStorage<Genre> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, this::createGenreFromRow);
    }

    private Genre createGenreFromRow(ResultSet resultSet, int id) throws SQLException {
        if (resultSet.getRow() == 0) {
            throw new GenreDoesNotExistException("Жанр не найден в БД.");
        }
        return new Genre(resultSet.getInt("GENRE_ID"), resultSet.getString("GENRE_NAME"));
    }

    @Override
    public Genre get(Integer id) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::createGenreFromRow, id);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }
}
