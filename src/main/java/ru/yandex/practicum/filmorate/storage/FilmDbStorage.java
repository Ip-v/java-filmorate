package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film save(Film film) {
        if (get(film.getId()) != null) {
            throw new ValidationException("Фильм с id = " + film.getId() + " уже есть в базе.");
        }
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("FILMS").usingGeneratedKeyColumns("FILM_ID");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("FILM_NAME", film.getName())
                .addValue("FILM_DESCRIPTION", film.getDescription())
                .addValue("RATING_ID", film.getMpa().getId())
                .addValue("FILM_RELEASE_DATE", film.getReleaseDate())
                .addValue("FILM_DURATION", film.getDuration());

        Number num = jdbcInsert.executeAndReturnKey(parameters);
        film.setId(num.intValue());
        updateGenresList(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (get(film.getId()) == null) {
            throw new FilmDoesNotExistException("Фильм не найден в базе. ID = " + film.getId());
        }
        String sql = "UPDATE films " +
                "SET FILM_NAME=?, FILM_DESCRIPTION=?, rating_id=?, FILM_RELEASE_DATE=?, FILM_DURATION=?  " +
                "WHERE FILM_ID=?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getMpa().getId(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
        );
        updateGenresList(film);
        updateUserLikes(film);
        return film;
    }

    private void updateUserLikes(Film film) {
        String deleteSql = "DELETE FROM LIKES WHERE FILM_ID=?";
        jdbcTemplate.update(deleteSql, film.getId());

        String insertSql = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?,?)";
        if (film.getLikes() != null) {
            film.getLikes().forEach(like -> jdbcTemplate.update(insertSql, film.getId(), like));
        }
    }

    private void updateGenresList(Film film) {
        String deleteSql = "DELETE FROM FILM_GENRES WHERE FILM_ID=?";
        jdbcTemplate.update(deleteSql, film.getId());

        String insertSql = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?,?)";
        if (film.getGenres() != null) {
            film.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .forEach(id -> jdbcTemplate.update(insertSql, film.getId(), id));
        }
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT * FROM FILMS F LEFT JOIN MPA M ON M.RATING_ID = F.RATING_ID";
        return jdbcTemplate.query(sql, this::createFilmFromRow);
    }

    @Override
    public Film get(Integer id) {
        String sql = "SELECT * FROM FILMS F " +
                "LEFT JOIN MPA M ON M.RATING_ID = F.RATING_ID " +
                "WHERE F.FILM_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::createFilmFromRow, id);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    private Film createFilmFromRow(ResultSet resultSet, int id) throws SQLException {
        if (resultSet.getRow() == 0) {
            throw new FilmDoesNotExistException("Фильм не найден в базе. ID = " + id);
        }
        Film film = new Film(
                resultSet.getInt("FILM_ID"),
                resultSet.getString("FILM_NAME"),
                resultSet.getString("FILM_DESCRIPTION"),
                resultSet.getDate("FILM_RELEASE_DATE").toLocalDate(),
                resultSet.getInt("FILM_DURATION"),
                null,
                null,
                new Mpa(resultSet.getInt("RATING_ID"), resultSet.getString("MPA_NAME"))
        );
        film.setGenres(getGenreListByFilmId(film.getId()));
        film.setLikes(getUserLikesByFilmId(film.getId()));
        return film;
    }

    private Set<Integer> getUserLikesByFilmId(Integer id) {
        String sql = "SELECT USER_ID FROM LIKES WHERE FILM_ID = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, num) -> rs.getInt("user_id"), id));
    }

    private Set<Genre> getGenreListByFilmId(Integer id) {
        String sql = "SELECT FG.GENRE_ID, GENRE_NAME FROM GENRES G " +
                "LEFT JOIN FILM_GENRES FG ON FG.GENRE_ID = G.GENRE_ID " +
                "WHERE FILM_ID = ?";
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(sql,
                (rs, num) -> new Genre(rs.getInt("genre_id"), rs.getString("genre_name")), id)
        );
        return genres.isEmpty() ? null : genres;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Film> getPopular(int number) {
        String sql = "SELECT F.FILM_ID FROM FILMS F " +
                "LEFT JOIN LIKES L ON F.FILM_ID = L.FILM_ID " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY count(L.FILM_ID) DESC " +
                "LIMIT ?";

        List<Integer> idList = jdbcTemplate.query(sql, rs -> {
            List<Integer> ids = new ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getInt("FILM_ID"));
            }
            return ids;
        }, number);
        sql = "SELECT * FROM FILMS F " +
                "JOIN MPA M ON M.RATING_ID = F.RATING_ID " +
                "WHERE F.FILM_ID IN (" + String.join(",", Collections.nCopies(idList.size(), "?")) + ")";
        return jdbcTemplate.query(sql, this::createFilmFromRow, idList.toArray());
    }
}
