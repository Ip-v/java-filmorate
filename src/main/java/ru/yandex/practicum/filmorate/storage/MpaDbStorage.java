package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MpaDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Сервис рейтингов
 */
@Component
@RequiredArgsConstructor
public class MpaDbStorage implements CatalogDBStorage<Mpa> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, this::createMpaFromRow);
    }

    private Mpa createMpaFromRow(ResultSet resultSet, int id) throws SQLException {
        if (resultSet.getRow() == 0) {
            throw new MpaDoesNotExistException("MPA не найдено в БД.");
        }
        return new Mpa(resultSet.getInt("RATING_ID"), resultSet.getString("MPA_NAME"));
    }

    @Override
    public Mpa get(Integer id) {
        String sql = "SELECT * FROM MPA WHERE RATING_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::createMpaFromRow, id);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }
}
