package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
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
@Primary
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
                .addValue("FILM_RATING_ID", film.getMpa().getId())
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
        String sql = "update films " +
                "set FILM_NAME=?, FILM_DESCRIPTION=?, rating_id=?, FILM_RELEASE_DATE=?, FILM_DURATION=?  " +
                "where FILM_ID=?";
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
        String deleteSql = "delete from likes where film_id=?";
        jdbcTemplate.update(deleteSql, film.getId());

        String insertSql = "insert into likes (film_id, user_id) values(?,?)";
        if (film.getUserLikes() != null) {
            film.getUserLikes().forEach(like -> jdbcTemplate.update(insertSql, film.getId(), like));
        }
    }

    private void updateGenresList(Film film) {
        String deleteSql = "delete from FILM_GENRES where FILM_ID=?";
        jdbcTemplate.update(deleteSql, film.getId());

        String insertSql = "insert into FILM_GENRES (FILM_ID, GENRE_ID) values(?,?)";
        if (film.getGenreList() != null) {
            film.getGenreList()
                    .stream()
                    .map(Genre::getId)
                    .forEach(id -> jdbcTemplate.update(insertSql, film.getId(), id));
        }
    }

    @Override
    public List<Film> getAll() {
        String sql = "select * from FILMS F left join MPA R on R.RATING_ID=F.RATING_ID";
        return jdbcTemplate.query(sql, this::createFilmFromRow);
    }

    @Override
    public Film get(int id) {
        String sql = "select * from films f " +
                "left join mpa m on m.rating_id=f.rating_id " +
                "where f.film_id=?";
        return jdbcTemplate.queryForObject(sql, this::createFilmFromRow, id);
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
        film.setGenreList(getGenreListByFilmId(film.getId()));
        film.setUserLikes(getUserLikesByFilmId(film.getId()));
        return film;
    }

    private Set<Integer> getUserLikesByFilmId(Integer id) {
        String sql = "select USER_ID from LIKES where FILM_ID=?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, num) -> rs.getInt("user_id"), id));
    }

    private Set<Genre> getGenreListByFilmId(Integer id) {
        String sql = "select fg.GENRE_ID, GENRE_NAME from genres g " +
                "left join film_genres fg on fg.GENRE_ID=g.GENRE_ID " +
                "where film_id=?";
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(sql,
                (rs, num) -> new Genre(rs.getInt("genre_id"), rs.getString("genre")), id)
        );
        return genres.isEmpty() ? null : genres;
    }

    @Override
    public void delete(int id) {
        String sql = "delete from FILMS WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Film> getPopular(int number) {
        String sql = "select F.FILM_ID from FILMS F " +
                "left join likes L on F.FILM_ID=L.film_id " +
                "group by F.FILM_ID " +
                "order by count(L.film_id) desc " +
                "limit ?";

        List<Integer> idList = jdbcTemplate.query(sql, rs -> {
            List<Integer> ids = new ArrayList<>();
            while (rs.next()) {
                ids.add(rs.getInt("FILM_ID"));
            }
            return ids;
        }, number);
        sql = "select * from FILMS F " +
                "join MPA M on M.RATING_ID = F.RATING_ID " +
                "where F.FILM_ID IN (" + String.join(",", Collections.nCopies(idList.size(), "?")) + ")";
        return jdbcTemplate.query(sql, this::createFilmFromRow, idList.toArray());
    }
}
